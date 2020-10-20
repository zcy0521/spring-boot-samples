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
        // 用户角色关系
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
    public List<RoleDO> findAllUserRole(Long userId) {
        // 用户角色关系
        UserRoleExample example = UserRoleExample.builder()
                .userId(userId)
                .build();
        List<UserRoleDO> userRoleList = userRoleMapper.selectAllByExample(example);

        // 角色ID
        Set<Long> roleIds = userRoleList.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toSet());

        return findAllByIds(roleIds);
    }

    @Override
    public List<RoleDO> findAllUserRole(Set<Long> userIds) {
        List<RoleDO> roleList = Lists.newArrayList();

        // 用户角色关系
        List<UserRoleDO> userRoleList = Lists.newArrayList();
        Iterables.partition(userIds, PARTITION_SIZE).forEach(userIdList -> {
            Set<Long> _userIds = Sets.newHashSet(userIdList);
            UserRoleExample example = UserRoleExample.builder().userIds(_userIds).build();
            List<UserRoleDO> _userRoleList = userRoleMapper.selectAllByExample(example);
            userRoleList.addAll(_userRoleList);
        });

        // 角色ID
        Set<Long> roleIds = userRoleList.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toSet());
        List<RoleDO> _roleList = findAllByIds(roleIds);

        // 角色不存在
        if (CollectionUtils.isEmpty(userIds)) {
            return roleList;
        }

        // 组装Roles集合 包含userId 用来区分不同用户
        Map<Long, RoleDO> roleIdMap = _roleList.stream().collect(Collectors.toMap(
                RoleDO::getId,
                Function.identity(),
                (first, second) -> first
        ));
        for (UserRoleDO userRole : userRoleList) {
            RoleDO _role = roleIdMap.get(userRole.getRoleId());
            RoleDO role = new RoleDO();
            role.setId(_role.getId());
            role.setRoleName(_role.getRoleName());
            role.setUserId(userRole.getId());
            roleList.add(role);
        }
        return roleList;
    }

    @Override
    public int removeUserRole(Long userId) {
        return removeUserRole(userId, null);
    }

    @Override
    public int removeUserRole(Long userId, Long roleId) {
        // 用户角色关系
        UserRoleExample example = UserRoleExample.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        List<UserRoleDO> userRole = userRoleMapper.selectAllByExample(example);

        // 用户角色不存在
        if (userRole == null) {
            return 0;
        }

        // 用户角色关系ID
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
