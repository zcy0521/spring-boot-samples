package com.sample.springboot.cache.redis.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.domain.RoleDO;
import com.sample.springboot.cache.redis.domain.UserRoleDO;
import com.sample.springboot.cache.redis.example.UserRoleExample;
import com.sample.springboot.cache.redis.mapper.RoleMapper;
import com.sample.springboot.cache.redis.mapper.UserRoleMapper;
import com.sample.springboot.cache.redis.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    /**
     * 超过100的数据查询分区
     */
    private static final int PARTITION_SIZE = 100;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<RoleDO> findAll() {
        return roleMapper.selectAll();
    }

    @Override
    public List<RoleDO> findAllByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        List<RoleDO> roleList = Lists.newArrayList();

        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<RoleDO> _roleList = roleMapper.selectAllByIds(_ids);
            roleList.addAll(_roleList);
        });

        return roleList;
    }

    @Override
    public RoleDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        return roleMapper.selectById(id);
    }

    @Override
    public Long insert(RoleDO entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        int count = roleMapper.insert(entity);
        return count > 0 ? entity.getId() : 0L;
    }

    @Override
    public boolean update(RoleDO entity) {
        if (null == entity.getId()) {
            throw new IllegalArgumentException();
        }

        int rows = roleMapper.update(entity);
        return rows > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        // 查询待删除部门是否存在
        RoleDO target = roleMapper.selectById(id);
        if (target == null) {
            return false;
        }

        // 删除无效用户角色
        removeUserRoleByRoleId(id);

        // 删除Role
        int count = roleMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<RoleDO> targetList = findAllByIds(ids);
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(RoleDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(targetIds)) {
            return 0;
        }

        // 删除无效附件
        removeUserRoleByRoleIds(targetIds);

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _targetIds = Sets.newHashSet(idList);
            roleMapper.deleteByIds(_targetIds);
        });
        return targetIds.size();
    }

    @Override
    public int deleteAll() {
        userRoleMapper.deleteAll();
        return roleMapper.deleteAll();
    }


    @Override
    public void saveUserRole(Long userId, Long roleId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(roleId);

        // 用户角色关系
        UserRoleExample example = UserRoleExample.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        UserRoleDO userRole = userRoleMapper.selectOneByExample(example);

        // 用户角色已存在
        if (userRole != null) {
            return;
        }

        // 创建用户角色
        userRole = new UserRoleDO();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
    }

    @Override
    public List<RoleDO> findAllUserRole(Long userId) {
        // 用户角色关系
        UserRoleExample example = UserRoleExample.builder()
                .userId(userId)
                .build();
        List<UserRoleDO> userRoleList = userRoleMapper.selectAllByExample(example);

        // 用户无角色
        if (CollectionUtils.isEmpty(userRoleList)) {
            return Lists.newArrayList();
        }

        // 角色ID集合
        Set<Long> roleIds = userRoleList.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toSet());

        return findAllByIds(roleIds);
    }

    @Override
    public List<RoleDO> findAllUserRole(Set<Long> userIds) {
        // 用户角色关系
        List<UserRoleDO> userRoleList = Lists.newArrayList();
        Iterables.partition(userIds, PARTITION_SIZE).forEach(userIdList -> {
            Set<Long> _userIds = Sets.newHashSet(userIdList);
            UserRoleExample example = UserRoleExample.builder().userIds(_userIds).build();
            List<UserRoleDO> _userRoleList = userRoleMapper.selectAllByExample(example);
            userRoleList.addAll(_userRoleList);
        });

        // 用户无角色
        if (CollectionUtils.isEmpty(userRoleList)) {
            return Lists.newArrayList();
        }

        // 查询角色集合
        Set<Long> roleIds = userRoleList.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toSet());
        List<RoleDO> roleList = findAllByIds(roleIds);

        // 角色不存在
        if (CollectionUtils.isEmpty(userIds)) {
            return Lists.newArrayList();
        }

        // 角色ID Map
        Map<Long, RoleDO> roleIdMap = roleList.stream().collect(Collectors.toMap(
                RoleDO::getId,
                Function.identity(),
                (first, second) -> first
        ));

        // 返回Roles集合 包含userId 用来区分不同用户的角色
        List<RoleDO> roles = Lists.newArrayList();
        userRoleList.forEach(userRole -> {
            RoleDO _role = roleIdMap.get(userRole.getRoleId());
            RoleDO role = new RoleDO();
            role.setId(_role.getId());
            role.setRoleName(_role.getRoleName());
            role.setUserId(userRole.getId());
            roles.add(role);
        });
        return roles;
    }

    @Override
    public boolean removeUserRole(UserRoleDO userRole) {
        if (userRole == null) {
            return false;
        }
        if (userRole.getId() == null) {
            return false;
        }

        // 查询待删除对象是否存在
        UserRoleDO target = userRoleMapper.selectById(userRole.getId());
        if (target == null) {
            return false;
        }

        int count = userRoleMapper.deleteById(target.getId());
        return count > 0;
    }

    @Override
    public int removeUserRoles(List<UserRoleDO> userRoles) {
        if (CollectionUtils.isEmpty(userRoles)) {
            return 0;
        }

        Set<Long> ids = userRoles.stream().map(UserRoleDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<UserRoleDO> targetList = Lists.newArrayList();
        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<UserRoleDO> _targetList = userRoleMapper.selectAllByIds(_ids);
            targetList.addAll(_targetList);
        });
        if (CollectionUtils.isEmpty(targetList)) {
            return 0;
        }

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(UserRoleDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(targetIds)) {
            return 0;
        }

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _targetIds = Sets.newHashSet(idList);
            userRoleMapper.deleteByIds(_targetIds);
        });
        return targetIds.size();
    }

    @Override
    public int removeUserRoleByUserId(Long userId) {
        UserRoleExample example = UserRoleExample
                .builder()
                .userId(userId)
                .build();
        List<UserRoleDO> userRoles = userRoleMapper.selectAllByExample(example);
        return removeUserRoles(userRoles);
    }

    @Override
    public int removeUserRoleByUserIds(Set<Long> userIds) {
        UserRoleExample example = UserRoleExample
                .builder()
                .userIds(userIds)
                .build();
        List<UserRoleDO> userRoles = userRoleMapper.selectAllByExample(example);
        return removeUserRoles(userRoles);
    }

    @Override
    public int removeUserRoleByRoleId(Long roleId) {
        UserRoleExample example = UserRoleExample
                .builder()
                .roleId(roleId)
                .build();
        List<UserRoleDO> userRoles = userRoleMapper.selectAllByExample(example);
        return removeUserRoles(userRoles);
    }

    @Override
    public int removeUserRoleByRoleIds(Set<Long> roleIds) {
        UserRoleExample example = UserRoleExample
                .builder()
                .roleIds(roleIds)
                .build();
        List<UserRoleDO> userRoles = userRoleMapper.selectAllByExample(example);
        return removeUserRoles(userRoles);
    }
}
