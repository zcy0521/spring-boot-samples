package com.sample.springboot.cache.redis.service.impl;

import com.google.common.collect.*;
import com.sample.springboot.cache.redis.domain.DeptAdminDO;
import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.enums.Position;
import com.sample.springboot.cache.redis.example.DeptAdminExample;
import com.sample.springboot.cache.redis.example.UserExample;
import com.sample.springboot.cache.redis.mapper.DeptAdminMapper;
import com.sample.springboot.cache.redis.mapper.DeptMapper;
import com.sample.springboot.cache.redis.mapper.UserMapper;
import com.sample.springboot.cache.redis.model.DeptDTO;
import com.sample.springboot.cache.redis.orika.DeptDTOMapper;
import com.sample.springboot.cache.redis.service.DeptService;
import com.sample.springboot.cache.redis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sample.springboot.cache.redis.mapper.sql.DeptSQLProvider.ROOT_PARENT_ID;

@Slf4j
@Service
public class DeptServiceImpl implements DeptService {

    /**
     * 部门树 nameSpace
     */
    private static final String DEPT_NAME_SPACE = "DeptTree";

    /**
     * 部门树每个Hash中Key最大数量
     */
    private static final int HASH_KEY_SIZE = 10000;

    /**
     * 从第几级路径开始显示名称
     */
    private static final int VISIBLE_LEVEL = 2;

    /**
     * 超过100的数据查询分区
     */
    private static final int PARTITION_SIZE = 100;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private DeptAdminMapper deptAdminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DeptDTOMapper deptDTOMapper;

    @PostConstruct
    public void buildTree() {
        log.info("初始化部门树...");
        StopWatch stopWatch = new StopWatch();

        // DB查询部门
        stopWatch.start("DB查询部门");
        List<DeptDO> deptList = deptMapper.selectTree();
        if (CollectionUtils.isEmpty(deptList)) {
            return;
        }
        stopWatch.stop();

        // DB查询部门管理员
        stopWatch.start("DB查询部门管理员");
        List<UserDO> adminList = findAllAdmin(null);
        if (!CollectionUtils.isEmpty(adminList)) {
            Map<Long, List<UserDO>> deptAdminMap = adminList.stream().collect(Collectors.groupingBy(
                    UserDO::getDeptId,
                    Collectors.mapping(Function.identity(), Collectors.toList())
            ));
            deptList.forEach(dept -> dept.setAdmins(deptAdminMap.get(dept.getId())));
        }
        stopWatch.stop();

        // 计算拆分的hashMap个数
        int mapSize = (deptList.size() + HASH_KEY_SIZE - 1) / HASH_KEY_SIZE;

        // 处理节点
        stopWatch.start("处理节点");
        // 部门ID映射
        Map<Long, DeptDO> idMap = deptList.stream().collect(Collectors.toMap(
                DeptDO::getId,
                Function.identity()
        ));
        // 部门ParentID映射
        Map<Long, List<DeptDO>> parentIdMap = deptList.stream().collect(Collectors.groupingBy(
                DeptDO::getParentId
        ));
        Multimap<String, DeptDTO> deptMultimap = ArrayListMultimap.create();
        for (DeptDO dept : deptList) {
            Long id = dept.getId();
            Long parentId = dept.getParentId();
            DeptDTO selfNode = deptDTOMapper.convertFrom(dept);

            // 存在父节点
            DeptDO parent = idMap.get(parentId);
            if (parent != null) {
                DeptDTO nodeParent = deptDTOMapper.convertFrom(parent);
                selfNode.setParent(nodeParent);
            }

            // 存在子节点
            List<DeptDO> children = parentIdMap.get(id);
            if (!CollectionUtils.isEmpty(children)) {
                List<DeptDTO> nodeChildren = deptDTOMapper.convertFromList(children);
                selfNode.setChildren(nodeChildren);
            }

            // ROOT至当前节点的数组
            DeptDTO[] nodeAncestors = pathToAncestors(dept, idMap);
            selfNode.setAncestors(nodeAncestors);

            // 添加节点
            String key = createKey(id, mapSize);
            deptMultimap.put(key, selfNode);
        }
        stopWatch.stop();

        // 清理部门缓存
        stopWatch.start("清理部门缓存");
        redisTemplate.delete(DEPT_NAME_SPACE);
        redisTemplate.delete(DEPT_NAME_SPACE + ":*");
        stopWatch.stop();

        // 将部门缓存至Redis
        stopWatch.start("将部门缓存至Redis");
        deptMultimap.asMap().forEach((key, value) -> {
            Map<String, DeptDTO> deptMap = value.stream().collect(Collectors.toMap(
                    dept -> String.valueOf(dept.getId()),
                    Function.identity(),
                    (first, second) -> first
            ));
            // 保存Map hash
            redisTemplate.opsForHash().putAll(key, deptMap);
            // 保存Key set
            redisTemplate.opsForSet().add(DEPT_NAME_SPACE, key);
        });
        stopWatch.stop();

        log.info("初始化部门树完成!");
        log.debug(stopWatch.prettyPrint());
        log.debug("初始化部门树总耗时: {}", stopWatch.getTotalTimeSeconds());
    }

    @Override
    public Long findRootId() {
        DeptDO root = deptMapper.selectTreeRoot();

        if (root == null) {
            log.warn("ROOT部门不存在 创建新ROOT部门!");
            root = new DeptDO();
            root.setDeptName("ROOT");
            root.setParentId(ROOT_PARENT_ID);
            deptMapper.insert(root);
        }

        return root.getId();
    }

    @Override
    public List<DeptDO> findAll() {
        return deptMapper.selectAll();
    }

    @Override
    public List<DeptDO> findAllByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        List<DeptDO> deptList = Lists.newArrayList();

        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<DeptDO> _deptList = deptMapper.selectAllByIds(_ids);
            deptList.addAll(_deptList);
        });

        return deptList;
    }

    @Override
    public DeptDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        StopWatch stopWatch = new StopWatch();

        // 查询部门Map数量
        stopWatch.start("查询部门Map数量");
        Long mapSize = redisTemplate.opsForSet().size(DEPT_NAME_SPACE);
        Objects.requireNonNull(mapSize);
        stopWatch.stop();

        // 计算部门所在Map的Key
        stopWatch.start("计算部门所在Map的Key");
        String key = createKey(id, Math.toIntExact(mapSize));
        log.debug("部门 [{}] 所在hashMap的Key: {}", id, key);
        stopWatch.stop();

        // Redis查询部门
        stopWatch.start("Redis查询部门");
        DeptDTO deptNode = (DeptDTO) redisTemplate.opsForHash().get(key, String.valueOf(id));
        Objects.requireNonNull(deptNode);
        stopWatch.stop();

        // 处理部门信息
        stopWatch.start("处理部门信息");
        DeptDO dept = deptDTOMapper.convertTo(deptNode);
        String path = ancestorsToPath(deptNode);
        dept.setPath(path);
        stopWatch.stop();

        log.info("查询部门: {} 成功", id);
        log.debug(stopWatch.prettyPrint());
        log.debug("查询部门: {} 总耗时: {}", id, stopWatch.getTotalTimeSeconds());
        return dept;
    }

    @Override
    public Long insert(DeptDO entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        int count = deptMapper.insert(entity);
        return count > 0 ? entity.getId() : 0L;
    }

    @Override
    public boolean update(DeptDO entity) {
        if (null == entity.getId()) {
            throw new IllegalArgumentException();
        }

        int count = deptMapper.update(entity);
        return count > 0;
    }

    @Override
    public int deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        // 查询待删除部门是否存在
        DeptDO target = deptMapper.selectById(id);
        if (null == target || ROOT_PARENT_ID.equals(target.getParentId())) {
            throw new IllegalArgumentException();
        }

        // 如果存在子部门 添加子部门ID
        Set<Long> targetIds =  Sets.newHashSet(target.getId());
        List<DeptDO> descendants = deptMapper.selectDescendants(id);
        if (!CollectionUtils.isEmpty(descendants)) {
            Set<Long> descendantIds = descendants.stream()
                    .map(DeptDO::getId)
                    .collect(Collectors.toSet());
            targetIds.addAll(descendantIds);
        }

        // 删除无效管理员
        removeAdminByDeptIds(targetIds);

        // 删除部门
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            // 部门ids
            Set<Long> _targetIds = Sets.newHashSet(idList);

            // 移动部门用户
            UserDO user = new UserDO();
            user.setDeptId(target.getParentId());
            UserExample example = UserExample
                    .builder()
                    .deptIds(_targetIds)
                    .build();
            userMapper.updateByExample(user, example);

            // 删除部门
            deptMapper.deleteByIds(_targetIds);
        });
        return targetIds.size();
    }

    @Override
    public int deleteAll() {
        // 删除管理员
        deptAdminMapper.deleteAll();
        // 删除部门
        return deptMapper.deleteAll();
    }

    @Override
    public void saveAdmin(Long deptId, Long userId, Position position) {
        Objects.requireNonNull(deptId);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(position);

        // 查询管理员
        DeptAdminExample example = DeptAdminExample.builder()
                .deptId(deptId)
                .userId(userId)
                .position(position)
                .build();
        DeptAdminDO deptAdmin = deptAdminMapper.selectOneByExample(example);

        // 管理员已存在
        if (deptAdmin != null) {
            return;
        }

        // 添加管理员
        deptAdmin = new DeptAdminDO();
        deptAdmin.setDeptId(deptId);
        deptAdmin.setUserId(userId);
        deptAdmin.setPosition(position);
        deptAdminMapper.insert(deptAdmin);
    }

    @Override
    public List<UserDO> findAllAdmin(Long deptId) {
        return findAllAdmin(deptId, null);
    }

    @Override
    public List<UserDO> findAllAdmin(Long deptId, Position position) {
        // 查询管理员
        DeptAdminExample example = DeptAdminExample.builder()
                .deptId(deptId)
                .position(position)
                .build();
        List<DeptAdminDO> deptAdminList = deptAdminMapper.selectAllByExample(example);

        // 无管理员
        if (CollectionUtils.isEmpty(deptAdminList)) {
            return Lists.newArrayList();
        }

        // 查询用户信息
        Set<Long> userIds = deptAdminList.stream()
                .map(DeptAdminDO::getUserId)
                .collect(Collectors.toSet());
        List<UserDO> userList = userService.findAllByIds(userIds);

        // 用户信息不存在
        if (CollectionUtils.isEmpty(userList)) {
            return Lists.newArrayList();
        }

        // 用户ID Map
        Map<Long, UserDO> userIdMap = userList.stream().collect(Collectors.toMap(
                UserDO::getId,
                Function.identity(),
                (first, second) -> first
        ));

        // 返回Users集合 包含deptId position 用来区分不同部门、职位的管理员
        List<UserDO> users = Lists.newArrayList();
        deptAdminList.forEach(deptAdmin -> {
            UserDO _user = userIdMap.get(deptAdmin.getUserId());
            UserDO user = new UserDO();
            user.setId(_user.getId());
            user.setUserName(_user.getUserName());
            user.setDeptId(deptAdmin.getDeptId());
            user.setPosition(deptAdmin.getPosition());
            users.add(user);
        });
        return users;
    }

    @Override
    public boolean removeAdmin(DeptAdminDO deptAdmin) {
        if (deptAdmin == null) {
            return false;
        }
        if (deptAdmin.getId() == null) {
            return false;
        }

        // 查询待删除对象是否存在
        DeptAdminDO target = deptAdminMapper.selectById(deptAdmin.getId());
        if (target == null) {
            return false;
        }

        int count = deptAdminMapper.deleteById(target.getId());
        return count > 0;
    }

    @Override
    public int removeAdmins(List<DeptAdminDO> deptAdmins) {
        if (CollectionUtils.isEmpty(deptAdmins)) {
            return 0;
        }

        Set<Long> ids = deptAdmins.stream().map(DeptAdminDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<DeptAdminDO> targetList = Lists.newArrayList();
        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<DeptAdminDO> _targetList = deptAdminMapper.selectAllByIds(_ids);
            targetList.addAll(_targetList);
        });
        if (CollectionUtils.isEmpty(targetList)) {
            return 0;
        }

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(DeptAdminDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(targetIds)) {
            return 0;
        }

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _targetIds = Sets.newHashSet(idList);
            deptAdminMapper.deleteByIds(_targetIds);
        });
        return targetIds.size();
    }

    @Override
    public int removeAdminByUserId(Long userId) {
        DeptAdminExample example = DeptAdminExample
                .builder()
                .userId(userId)
                .build();
        List<DeptAdminDO> admins = deptAdminMapper.selectAllByExample(example);
        return removeAdmins(admins);
    }

    @Override
    public int removeAdminByUserIds(Set<Long> userIds) {
        DeptAdminExample example = DeptAdminExample
                .builder()
                .userIds(userIds)
                .build();
        List<DeptAdminDO> admins = deptAdminMapper.selectAllByExample(example);
        return removeAdmins(admins);
    }

    @Override
    public int removeAdminByDeptId(Long deptId) {
        DeptAdminExample example = DeptAdminExample
                .builder()
                .deptId(deptId)
                .build();
        List<DeptAdminDO> admins = deptAdminMapper.selectAllByExample(example);
        return removeAdmins(admins);
    }

    @Override
    public int removeAdminByDeptIds(Set<Long> deptIds) {
        DeptAdminExample example = DeptAdminExample
                .builder()
                .deptIds(deptIds)
                .build();
        List<DeptAdminDO> admins = deptAdminMapper.selectAllByExample(example);
        return removeAdmins(admins);
    }

    /**
     * 创建部门树Redis key
     */
    private static String createKey(Long id, int mapSize) {
        int hash = Long.hashCode(id);
        return DEPT_NAME_SPACE + ":" + (hash % mapSize);
    }

    /**
     * 获取ROOT至当前节点的数组
     */
    private DeptDTO[] pathToAncestors(DeptDO dept, Map<Long, DeptDO> deptMap) {
        String[] path = dept.getPath().split(":");
        Objects.requireNonNull(path);

        DeptDTO[] ancestors = new DeptDTO[path.length];
        for(int counter = 0; counter < path.length; counter++) {
            Long id = Long.valueOf(path[counter]);
            DeptDO ancestor = deptMap.get(id);
            ancestors[counter] = deptDTOMapper.convertFrom(ancestor);
        }

        return ancestors;
    }

    /**
     * 获取节点全路径
     */
    private String ancestorsToPath(DeptDTO deptNode) {
        DeptDTO[] ancestors = deptNode.getAncestors();
        Objects.requireNonNull(ancestors);

        if (ancestors.length <= VISIBLE_LEVEL) {
            return deptNode.getDeptName();
        }

        String[] path = new String[ancestors.length - VISIBLE_LEVEL];
        for(int counter = VISIBLE_LEVEL; counter < ancestors.length; counter++) {
            DeptDTO ancestor = ancestors[counter];
            path[counter - VISIBLE_LEVEL] = ancestor.getDeptName();
        }

        return String.join("-", path);
    }

}
