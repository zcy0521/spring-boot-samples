package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.RoleDO;
import com.sample.springboot.cache.redis.domain.UserRoleDO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleService {

    List<RoleDO> findAll();

    List<RoleDO> findAllByIds(Set<Long> ids);

    RoleDO findById(Long id);

    Long insert(RoleDO entity);

    boolean update(RoleDO entity);

    boolean deleteById(Long id);

    int deleteByIds(Set<Long> ids);

    int deleteAll();



    void saveUserRole(Long userId, Long roleId);

    List<RoleDO> findAllUserRole(Long userId);

    List<RoleDO> findAllUserRole(Set<Long> userIds);

    boolean removeUserRole(UserRoleDO userRole);

    int removeUserRoles(List<UserRoleDO> userRoles);

    int removeUserRoleByUserId(Long userId);

    int removeUserRoleByUserIds(Set<Long> userIds);

    int removeUserRoleByRoleId(Long roleId);

    int removeUserRoleByRoleIds(Set<Long> roleIds);

}
