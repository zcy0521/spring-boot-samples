package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.query.UserQuery;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {

    List<UserDO> findAll();

    List<UserDO> findAll(UserQuery query, int number, int size);

    List<UserDO> findAllByIds(Set<Long> ids);

    UserDO findById(Long id);

    Long insert(UserDO entity);

    boolean update(UserDO entity);

    boolean deleteById(Long id);

    int deleteByIds(Set<Long> ids);

    int deleteAll();

}
