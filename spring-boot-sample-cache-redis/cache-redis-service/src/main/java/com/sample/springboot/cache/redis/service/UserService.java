package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.query.UserQuery;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserDO> findAll();

    List<UserDO> findAll(int pageNumber, int pageSize);

    List<UserDO> findAll(UserQuery query);

    List<UserDO> findAll(UserQuery query, int pageNumber, int pageSize);

    UserDO findById(Long id);

    UserDO findOne(UserQuery query);

    Long insert(UserDO entity);

    boolean update(UserDO entity);

    boolean deleteById(Long id);

}
