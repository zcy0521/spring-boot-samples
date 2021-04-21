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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * 超过100的数据查询分区
     */
    private static final int PARTITION_SIZE = 100;

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

        // 构建Page对象
        int totalElements = userMapper.countByExample(example);
        Page page = Page.builder()
                .number(query.getNumber())
                .size(query.getSize())
                .totalElements(totalElements)
                .build();

        // 开启分页查询
        page.startPage();
        List<UserDO> userList = userMapper.selectAllByExample(example);

        // 处理用户关联信息
        handleUserList(userList);
        // 将分页对象通过查询对象返回
        query.setPage(page);
        return userList;
    }

    @Override
    public List<UserDO> findAllByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        // 分区 IN 查询
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

        // 查询用户信息
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
    public boolean update(UserDO entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException();
        }

        int rows = userMapper.update(entity);
        return rows > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        // 查询待删除用户是否存在
        UserDO target = userMapper.selectById(id);
        if (target == null) {
            return false;
        }

        // 删除订单
        orderService.deleteByUserId(id);
        // 删除角色
        roleService.removeUserRoleByUserId(id);
        // 删除管理员
        deptService.removeAdminByUserId(id);

        // 删除用户
        int count = userMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<UserDO> targetList = findAllByIds(ids);
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(UserDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(targetIds)) {
            return 0;
        }

        // 删除订单
        orderService.deleteByUserIds(targetIds);
        // 删除角色
        roleService.removeUserRoleByUserIds(targetIds);
        // 删除管理员
        deptService.removeAdminByUserIds(targetIds);

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _targetIds = Sets.newHashSet(idList);
            userMapper.deleteByIds(_targetIds);
        });
        return targetIds.size();
    }

    @Override
    public int deleteAll() {
        // 删除订单
        orderService.deleteAll();
        // 删除角色
        userRoleMapper.deleteAll();
        // 删除管理员
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
