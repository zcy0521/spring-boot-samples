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
import com.sample.springboot.cache.redis.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl extends BaseService implements RoleService {

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
    public Map<Long, RoleDO> findIdMapByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        List<RoleDO> roles = findAllByIds(ids);

        return roles.stream().collect(Collectors.toMap(
                RoleDO::getId,
                Function.identity(),
                (first, second) -> first
        ));
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
    public Boolean update(RoleDO entity) {
        if (null == entity.getId()) {
            throw new IllegalArgumentException();
        }

        int rows = roleMapper.update(entity);
        return rows > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        RoleDO role = roleMapper.selectById(id);
        if (role == null) {
            throw new IllegalArgumentException();
        }

        // 删除无效用户角色
        removeUserRole(null, id);

        // 删除Role
        int count = roleMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteAll() {
        userRoleMapper.deleteAll();
        return roleMapper.deleteAll();
    }


    @Override
    public Boolean saveUserRole(Long userId, Long roleId) {
        // 查询用户角色
        UserRoleExample example = UserRoleExample.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        UserRoleDO userRole = userRoleMapper.selectOneByExample(example);

        // 用户角色已存在
        if (userRole != null) {
            return true;
        }

        // 创建用户角色
        userRole = new UserRoleDO();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        int count = userRoleMapper.insert(userRole);
        return count > 0;
    }

    @Override
    public List<RoleDO> findUserRoles(Long userId) {
        UserRoleExample example = UserRoleExample.builder()
                .userId(userId)
                .build();
        List<UserRoleDO> userRoles = userRoleMapper.selectAllByExample(example);

        // 查询Roles集合
        Set<Long> rolesIds = userRoles.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toSet());

        return findAllByIds(rolesIds);
    }

    @Override
    public List<RoleDO> findUsersRoles(Set<Long> userIds) {
        List<UserRoleDO> userRoles = Lists.newArrayList();

        Iterables.partition(userIds, PARTITION_SIZE).forEach(userIdList -> {
            Set<Long> _userIds = Sets.newHashSet(userIdList);
            UserRoleExample example = UserRoleExample.builder().userIds(_userIds).build();
            List<UserRoleDO> _userRoles = userRoleMapper.selectAllByExample(example);
            userRoles.addAll(_userRoles);
        });

        // 查询Roles集合
        Set<Long> rolesIds = userRoles.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toSet());
        Map<Long, RoleDO> roleIdMap = findIdMapByIds(rolesIds);

        // 返回Roles 对象中包含userId 用来区分不同用户
        List<RoleDO> roles = Lists.newArrayList();
        for (UserRoleDO userRole : userRoles) {
            RoleDO _role = roleIdMap.get(userRole.getRoleId());
            RoleDO role = new RoleDO();
            role.setId(_role.getId());
            role.setRoleName(_role.getRoleName());
            role.setUserId(userRole.getId());
            roles.add(role);
        }
        return roles;
    }

    @Override
    public int removeUserRole(Long userId) {
        return removeUserRole(userId, null);
    }

    @Override
    public int removeUserRole(Long userId, Long roleId) {
        // 查询用户角色信息
        UserRoleExample example = UserRoleExample.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        List<UserRoleDO> userRole = userRoleMapper.selectAllByExample(example);

        // 用户角色不存在
        if (userRole == null) {
            return 0;
        }

        Set<Long> userRoleIds = userRole.stream()
                .map(UserRoleDO::getId)
                .collect(Collectors.toSet());

        // 删除用户角色
        Iterables.partition(userRoleIds, PARTITION_SIZE).forEach(userRoleIdList -> {
            Set<Long> _userRoleIds = Sets.newHashSet(userRoleIdList);
            userRoleMapper.deleteByIds(_userRoleIds);
        });

        return userRoleIds.size();
    }

}
