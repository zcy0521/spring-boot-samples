package com.sample.springboot.cache.redis.service.impl;

import com.sample.springboot.cache.redis.common.CachingNames;
import com.sample.springboot.cache.redis.domain.*;
import com.sample.springboot.cache.redis.mapper.*;
import com.sample.springboot.cache.redis.query.UserQuery;
import com.sample.springboot.cache.redis.service.UserService;
import com.sample.springboot.cache.redis.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@CacheConfig(cacheNames = CachingNames.USER_CACHE)
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Cacheable
    public List<UserDO> findAll() {
        List<UserDO> users = userMapper.selectAll();
        handleUsers(users);
        return users;
    }

    @Override
    @Cacheable(key = "#pageNumber.concat('-').concat(#pageSize)")
    public List<UserDO> findAll(int pageNumber, int pageSize) {
        startPage(pageNumber, pageSize);
        List<UserDO> users = userMapper.selectAll();
        handleUsers(users);
        return users;
    }

    @Override
    @Cacheable
    public List<UserDO> findAll(UserQuery query) {
        Example example = Example.builder(UserDO.class)
                .where(buildWhereSqls(query))
                .orderByDesc("gmt_create")
                .build();
        List<UserDO> users = userMapper.selectByExample(example);
        handleUsers(users);
        return users;
    }



    @Override
    @Cacheable(key = "#pageNumber.concat('-').concat(#pageSize)")
    public List<UserDO> findAll(UserQuery query, int pageNumber, int pageSize) {
        Example example = Example.builder(UserDO.class)
                .where(buildWhereSqls(query))
                .orderByDesc("gmt_create")
                .build();
        startPage(pageNumber, pageSize);
        List<UserDO> users = userMapper.selectByExample(example);
        handleUsers(users);
        return users;
    }

    @Override
    @Cacheable(key = "#id")
    public UserDO findById(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ID must not be null!");
        }

        UserDO user = userMapper.selectByPrimaryKey(id);
        handleUser(user);
        return user;
    }

    @Override
    @Caching(
            cacheable = {
                    @Cacheable(key = "#result.id"),
                    @Cacheable(key = "#result.userName")
            }
    )
    public UserDO findOne(UserQuery query) {
        Example example = Example.builder(UserDO.class)
                .where(buildWhereSqls(query))
                .build();
        UserDO user = userMapper.selectOneByExample(example);
        handleUser(user);
        return user;
    }

    @Override
    @Caching(
            put = {
                    @CachePut(key = "#result"),
                    @CachePut(key = "#entity.userName"),
            },
            evict = {
                    @CacheEvict(allEntries = true)
            }
    )
    public Long insert(UserDO entity) {
        if (null == entity) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        int rows = userMapper.insertSelective(entity);
        if (rows > 0) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    @Caching(
            put = {
                    @CachePut(key = "#entity.id"),
                    @CachePut(key = "#entity.userName"),
            },
            evict = {
                    @CacheEvict(allEntries = true)
            }
    )
    public boolean update(UserDO entity) {
        if (null == entity) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        if (null == entity.getId()) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        int rows = userMapper.updateByPrimaryKeySelective(entity);
        return rows > 0;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "#id"),
                    @CacheEvict(allEntries = true)
            }
    )
    public boolean deleteById(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        int rows = userMapper.deleteByPrimaryKey(id);
        return rows > 0;
    }

    /**
     * 根据查询对象生成查询条件
     *
     * @param query 查询对象
     * @return {@link WeekendSqls}
     */
    private WeekendSqls<UserDO> buildWhereSqls(UserQuery query) {
        WeekendSqls<UserDO> sqls = WeekendSqls.custom();
        // 无查询条件
        if (null == query) {
            return sqls;
        }
        // 用户名
        if(query.getUserName() != null){
            sqls.andLike(UserDO::getDeptId, "%".concat(query.getUserName()).concat("%"));
        }
        // 所在部门
        if(query.getDeptId() != null){
            sqls.andEqualTo(UserDO::getUserName, query.getDeptId());
        }
        return sqls;
    }

    /**
     * 处理users
     *
     * @param users 用户集合
     */
    private void handleUsers(List<UserDO> users) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }

        // userIds
        Set<Long> userIds = users.stream().map(UserDO::getId).collect(Collectors.toSet());
        // deptIds
        Set<Long> deptIds = users.stream().map(UserDO::getDeptId).collect(Collectors.toSet());

        // 查询部门集合
        List<DeptDO> depts = deptMapper.selectByIds(deptIds);
        Map<Long, DeptDO> deptIdMap = depts.stream().collect(Collectors.toMap(
                DeptDO::getId,
                Function.identity(),
                (first, second) -> first
        ));

        // 查询订单集合
        List<OrderDO> orders = orderMapper.selectByUserIds(userIds);
        Map<Long, List<OrderDO>> userIdOrdersMap = orders.stream().collect(Collectors.groupingBy(
                OrderDO::getUserId
        ));

        // 查询用户角色关系集合
        List<UserRoleDO> userRoles = userRoleMapper.selectByUserIds(userIds);
        Map<Long, Set<Long>> userIdRoleIdsMap = userRoles.stream().collect(Collectors.groupingBy(
                UserRoleDO::getUserId,
                Collectors.mapping(UserRoleDO::getRoleId, Collectors.toSet())
        ));
        // roleIds
        Set<Long> roleIds = userIdRoleIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        // 查询角色集合
        List<RoleDO> roles = roleMapper.selectByIds(roleIds);
        Map<Long, RoleDO> roleIdMap = roles.stream().collect(Collectors.toMap(
                RoleDO::getId,
                Function.identity(),
                (first, second) -> first
        ));
        Map<Long, List<RoleDO>> userIdRolesMap = userIdRoleIdsMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream().map(roleIdMap::get).collect(Collectors.toList())
        ));

        users.forEach(user -> {
            user.setDept(deptIdMap.get(user.getDeptId()));
            user.setOrders(userIdOrdersMap.get(user.getId()));
            user.setRoles(userIdRolesMap.get(user.getId()));
        });
    }

    /**
     * 处理user
     *
     * @param user 用户对象
     */
    private void handleUser(UserDO user) {
        if (null == user) {
            return;
        }

        // userId
        Long userId = user.getId();
        // deptId
        Long deptId = user.getDeptId();

        // 查询部门对象
        DeptDO dept = deptMapper.selectByPrimaryKey(deptId);

        // 查询订单集合
        List<OrderDO> orders = orderMapper.selectByUserId(userId);

        // 查询用户角色关系集合
        List<UserRoleDO> userRoles = userRoleMapper.selectByUserId(userId);
        // roleIds
        Set<Long> roleIds = userRoles.stream().map(UserRoleDO::getRoleId).collect(Collectors.toSet());
        // 查询角色集合
        List<RoleDO> roles = roleMapper.selectByIds(roleIds);

        user.setDept(dept);
        user.setOrders(orders);
        user.setRoles(roles);
    }

}
