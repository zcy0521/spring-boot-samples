package com.sample.springboot.cache.redis.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.domain.*;
import com.sample.springboot.cache.redis.example.UserExample;
import com.sample.springboot.cache.redis.mapper.*;
import com.sample.springboot.cache.redis.page.Page;
import com.sample.springboot.cache.redis.query.UserQuery;
import com.sample.springboot.cache.redis.service.DeptService;
import com.sample.springboot.cache.redis.service.OrderService;
import com.sample.springboot.cache.redis.service.RoleService;
import com.sample.springboot.cache.redis.service.UserService;
import com.sample.springboot.cache.redis.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends BaseService implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private DeptAdminMapper deptAdminMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private OrderService orderService;

    @Override
    public List<UserDO> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<UserDO> findAll(UserQuery query, int number, int size) {
        if (query == null) {
            query = new UserQuery();
        }

        // 查询条件
        UserExample example = UserExample.builder()
                .userName(query.getUserName())
                .deptId(query.getDeptId())
                .deptIds(query.getDeptIds())
                .deleted(query.getDeleted())
                .build();

        // 查询分页对象 优化number
        int totalElements = userMapper.countByExample(example);
        Page page = new Page(number, size, totalElements);
        query.setPage(page);

        // 分页查询
        startPage(page.getNumber(), page.getSize());
        List<UserDO> userList = userMapper.selectAllByExample(example);
        handleUserList(userList);
        return userList;
    }

    @Override
    public List<UserDO> findAllByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        List<UserDO> userList = Lists.newArrayList();

        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<UserDO> _userList = userMapper.selectAllByIds(_ids);
            userList.addAll(_userList);
        });

        return userList;
    }

    @Override
    public UserDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        UserDO user = userMapper.selectById(id);

        // 部门
        DeptDO dept = deptService.findById(user.getDeptId());
        user.setDept(dept);

        // 订单集合
        List<OrderDO> orderList = orderService.findAllByUserId(id);
        user.setOrders(orderList);

        // 角色集合
        List<RoleDO> roleList = roleService.findAllUserRole(id);
        user.setRoles(roleList);

        return user;
    }

    @Override
    public Long insert(UserDO entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        int count = userMapper.insert(entity);
        return count > 0 ? entity.getId() : 0L;
    }

    @Override
    public Boolean update(UserDO entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException();
        }

        int rows = userMapper.update(entity);
        return rows > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        // 订单
        orderService.deleteByUserId(id);
        // 角色
        roleService.removeUserRole(id);
        // 部门管理员
        deptService.removeDeptAdmin(id);

        int count = userMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteAll() {
        // 订单
        orderService.deleteAll();
        // 角色
        userRoleMapper.deleteAll();
        // 部门管理员
        deptAdminMapper.deleteAll();

        return userMapper.deleteAll();
    }

    /**
     * 处理 userList
     *
     * @param userList 用户集合
     */
    private void handleUserList(List<UserDO> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }

        // 用户ID
        Set<Long> userIds = userList.stream()
                .map(UserDO::getId)
                .collect(Collectors.toSet());

        // 部门ID
        Set<Long> deptIds = userList.stream()
                .map(UserDO::getDeptId)
                .collect(Collectors.toSet());

        // 部门
        List<DeptDO> deptList = deptService.findAllByIds(deptIds);
        if (!CollectionUtils.isEmpty(deptList)) {
            Map<Long, DeptDO> deptIdMap = deptList.stream().collect(Collectors.toMap(
                    DeptDO::getId,
                    Function.identity(),
                    (first, second) -> first
            ));
            userList.forEach(user -> user.setDept(deptIdMap.get(user.getDeptId())));
        }

        // 订单
        List<OrderDO> orderList = orderService.findAllByUserIds(userIds);
        if (!CollectionUtils.isEmpty(orderList)) {
            Map<Long, List<OrderDO>> userOrdersMap = orderList.stream().collect(Collectors.groupingBy(
                    OrderDO::getUserId
            ));
            userList.forEach(user -> user.setOrders(userOrdersMap.get(user.getId())));
        }

        // 角色
        List<RoleDO> roleList = roleService.findAllUserRole(userIds);
        if (!CollectionUtils.isEmpty(roleList)) {
            Map<Long, List<RoleDO>> userRolesMap = roleList.stream().collect(Collectors.groupingBy(
                    RoleDO::getUserId
            ));
            userList.forEach(user -> user.setRoles(userRolesMap.get(user.getId())));
        }

    }

}
