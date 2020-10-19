package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.RoleDO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleService {

    List<RoleDO> findAll();

    List<RoleDO> findAllByIds(Set<Long> ids);

    Map<Long, RoleDO> findIdMapByIds(Set<Long> ids);

    RoleDO findById(Long id);

    Long insert(RoleDO entity);

    Boolean update(RoleDO entity);

    Boolean deleteById(Long id);

    int deleteAll();

    /**
     * 新增用户角色
     */
    Boolean saveUserRole(Long userId, Long roleId);

    /**
     * 查询用户角色
     */
    List<RoleDO> findUserRoles(Long userId);

    /**
     * 查询用户角色
     */
    List<RoleDO> findUsersRoles(Set<Long> userIds);

    /**
     * 移除用户角色
     */
    int removeUserRole(Long userId);

    /**
     * 移除用户角色
     */
    int removeUserRole(Long userId, Long roleId);

}
