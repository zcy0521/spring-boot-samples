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
import com.sample.springboot.cache.redis.orika.mapper.DeptDTOMapper;
import com.sample.springboot.cache.redis.service.DeptService;
import com.sample.springboot.cache.redis.service.UserService;
import com.sample.springboot.cache.redis.service.base.BaseService;
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
public class DeptServiceImpl extends BaseService implements DeptService {

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

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptAdminMapper deptAdminMapper;

    @Autowired
    private DeptDTOMapper deptDTOMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void buildTree() {
        log.info("初始化部门树...");
        StopWatch stopWatch = new StopWatch();

        // DB查询部门
        stopWatch.start("DB查询部门");
        List<DeptDO> deptList = deptMapper.selectTree();
        Objects.requireNonNull(deptList);
        stopWatch.stop();

        // 计算拆分的hashMap个数
        int mapSize = (deptList.size() + HASH_KEY_SIZE - 1) / HASH_KEY_SIZE;

        // DB查询部门管理员
        stopWatch.start("DB查询部门管理员");
        List<UserDO> adminList = findDeptAdmins(null);
        Map<Long, List<UserDO>> deptAdminsMap = adminList.stream().collect(Collectors.groupingBy(
                UserDO::getDeptId,
                Collectors.mapping(Function.identity(), Collectors.toList())
        ));
        deptList.forEach(dept -> dept.setAdmins(deptAdminsMap.get(dept.getId())));
        stopWatch.stop();

        // 处理节点
        stopWatch.start("处理节点");
        Map<Long, DeptDO> idMap = deptList.stream().collect(Collectors.toMap(DeptDO::getId, Function.identity()));
        Map<Long, List<DeptDO>> parentIdMap = deptList.stream().collect(Collectors.groupingBy(DeptDO::getParentId));
        Multimap<String, DeptDTO> deptMultimap = ArrayListMultimap.create();
        for (DeptDO dept : deptList) {
            Long id = dept.getId();
            Long parentId = dept.getParentId();
            DeptDTO selfNode = deptDTOMapper.convertFrom(dept);

            // 有父节点
            DeptDO parent = idMap.get(parentId);
            if (parent != null) {
                DeptDTO nodeParent = deptDTOMapper.convertFrom(parent);
                selfNode.setParent(nodeParent);
            }

            // 有子节点
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
    public Map<Long, DeptDO> findIdMapByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        List<DeptDO> deptList = findAllByIds(ids);

        return deptList.stream().collect(Collectors.toMap(
                DeptDO::getId,
                Function.identity(),
                (first, second) -> first
        ));
    }

    @Override
    public DeptDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        StopWatch stopWatch = new StopWatch();

        // 查询部门所在hashMap的Key
        stopWatch.start("查询部门所在hashMap的Key");
        Long mapSize = redisTemplate.opsForSet().size(DEPT_NAME_SPACE);
        Objects.requireNonNull(mapSize);
        String key = createKey(id, Math.toIntExact(mapSize));
        log.debug("部门 [{}] 所在hashMap的Key: {}", id, key);
        stopWatch.stop();

        // Redis中查询部门
        stopWatch.start("Redis查询部门");
        DeptDTO deptNode = (DeptDTO) redisTemplate.opsForHash().get(key, String.valueOf(id));
        Objects.requireNonNull(deptNode);
        stopWatch.stop();

        // 部门信息
        DeptDO dept = deptDTOMapper.convertTo(deptNode);
        Objects.requireNonNull(dept);

        // 部门路径
        String path = ancestorsToPath(deptNode);
        dept.setPath(path);

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
    public Boolean update(DeptDO entity) {
        if (null == entity.getId()) {
            throw new IllegalArgumentException("ID must not be null!");
        }

        int count = deptMapper.update(entity);
        return count > 0;
    }

    @Override
    public int deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        DeptDO dept = deptMapper.selectById(id);
        if (null == dept || ROOT_PARENT_ID.equals(dept.getParentId())) {
            throw new IllegalArgumentException();
        }

        // 部门ID集合
        Set<Long> deptIds =  Sets.newHashSet(id);
        List<DeptDO> descendants = deptMapper.selectDescendants(id);
        if (!CollectionUtils.isEmpty(descendants)) {
            Set<Long> descendantIds = descendants.stream()
                    .map(DeptDO::getId)
                    .collect(Collectors.toSet());
            deptIds.addAll(descendantIds);
        }

        // 移动用户 删除部门
        Iterables.partition(deptIds, PARTITION_SIZE).forEach(deptIdList -> {
            // 部门ids
            Set<Long> _deptIds = Sets.newHashSet(deptIdList);

            // 移动子部门用户
            UserDO user = new UserDO();
            user.setDeptId(dept.getParentId());
            UserExample example = UserExample.builder().deptIds(_deptIds).build();
            userMapper.updateByExample(user, example);

            // 删除部门
            deptMapper.deleteByIds(_deptIds);
        });

        // 删除无效部门管理员
        removeDeptAdmin(null, id, null);

        return deptIds.size();
    }

    @Override
    public int deleteAll() {
        deptAdminMapper.deleteAll();
        return deptMapper.deleteAll();
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
    public Boolean saveDeptAdmin(Long deptId, Long userId, Position position) {
        // 查询管理员信息
        DeptAdminExample example = DeptAdminExample.builder()
                .deptId(deptId)
                .adminId(userId)
                .position(position)
                .build();
        DeptAdminDO deptAdmin = deptAdminMapper.selectOneByExample(example);

        // 管理员已存在
        if (deptAdmin != null) {
            return true;
        }

        // 添加管理员
        deptAdmin = new DeptAdminDO();
        deptAdmin.setDeptId(deptId);
        deptAdmin.setUserId(userId);
        deptAdmin.setPosition(position);
        int count = deptAdminMapper.insert(deptAdmin);
        return count > 0;
    }

    @Override
    public List<UserDO> findDeptAdmins(Long deptId) {
        return findDeptAdmins(deptId, null);
    }

    @Override
    public List<UserDO> findDeptAdmins(Long deptId, Position position) {
        DeptAdminExample example = DeptAdminExample.builder()
                .deptId(deptId)
                .position(position)
                .build();
        List<DeptAdminDO> deptAdmins = deptAdminMapper.selectAllByExample(example);

        // 查询User集合
        Set<Long> userIds = deptAdmins.stream()
                .map(DeptAdminDO::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserDO> userIdMap = userService.findIdMapByIds(userIds);

        // 组装Users 对象中包含deptId position 用来区分不同部门、职位
        List<UserDO> adminList = Lists.newArrayList();
        for (DeptAdminDO deptAdmin : deptAdmins) {
            UserDO user = userIdMap.get(deptAdmin.getUserId());
            UserDO admin = new UserDO();
            admin.setId(user.getId());
            admin.setUserName(user.getUserName());
            admin.setDeptId(deptAdmin.getDeptId());
            admin.setPosition(deptAdmin.getPosition());
            adminList.add(admin);
        }
        return adminList;
    }

    @Override
    public int removeDeptAdmin(Long userId) {
        return removeDeptAdmin(userId, null, null);
    }

    @Override
    public int removeDeptAdmin(Long userId, Long deptId) {
        return removeDeptAdmin(userId, deptId, null);
    }

    @Override
    public int removeDeptAdmin(Long userId, Long deptId, Position position) {
        // 查询管理员信息
        DeptAdminExample example = DeptAdminExample.builder()
                .deptId(deptId)
                .adminId(userId)
                .position(position)
                .build();
        List<DeptAdminDO> deptAdminList = deptAdminMapper.selectAllByExample(example);

        // 管理员信息不存在
        if (CollectionUtils.isEmpty(deptAdminList)) {
            return 0;
        }

        Set<Long> deptAdminIds = deptAdminList.stream()
                .map(DeptAdminDO::getId)
                .collect(Collectors.toSet());

        // 删除管理员记录
        Iterables.partition(deptAdminIds, PARTITION_SIZE).forEach(deptAdminIdList -> {
            Set<Long> _deptAdminIds = Sets.newHashSet(deptAdminIdList);
            deptAdminMapper.deleteByIds(_deptAdminIds);
        });

        return deptAdminIds.size();
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
