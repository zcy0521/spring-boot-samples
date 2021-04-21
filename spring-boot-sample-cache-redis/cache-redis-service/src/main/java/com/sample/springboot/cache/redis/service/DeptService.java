package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.DeptAdminDO;
import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.enums.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeptService {

    Long findRootId();

    List<DeptDO> findAll();

    List<DeptDO> findAllByIds(Set<Long> ids);

    DeptDO findById(Long id);

    Long insert(DeptDO entity);

    boolean update(DeptDO entity);

    int deleteById(Long id);

    int deleteAll();



    void saveAdmin(Long deptId, Long userId, Position position);

    List<UserDO> findAllAdmin(Long deptId);

    List<UserDO> findAllAdmin(Long deptId, Position position);

    boolean removeAdmin(DeptAdminDO deptAdmin);

    int removeAdmins(List<DeptAdminDO> deptAdmins);

    int removeAdminByUserId(Long userId);

    int removeAdminByUserIds(Set<Long> userIds);

    int removeAdminByDeptId(Long deptId);

    int removeAdminByDeptIds(Set<Long> deptIds);

}
