package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.enums.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeptService {

    List<DeptDO> findAll();

    List<DeptDO> findAllByIds(Set<Long> ids);

    Map<Long, DeptDO> findIdMapByIds(Set<Long> ids);

    DeptDO findById(Long id);

    Long insert(DeptDO entity);

    Boolean update(DeptDO entity);

    int deleteById(Long id);

    int deleteAll();

    /**
     * ROOT部门ID
     */
    Long findRootId();

    /**
     * 新增部门管理员
     */
    Boolean saveDeptAdmin(Long deptId, Long userId, Position position);

    /**
     * 查询部门管理员
     */
    List<UserDO> findDeptAdmins(Long deptId);

    /**
     * 查询部门管理员
     */
    List<UserDO> findDeptAdmins(Long deptId, Position position);

    /**
     * 删除部门管理员
     */
    int removeDeptAdmin(Long userId);

    /**
     * 删除部门管理员
     */
    int removeDeptAdmin(Long userId, Long deptId);

    /**
     * 删除部门管理员
     */
    int removeDeptAdmin(Long userId, Long deptId, Position position);

}
