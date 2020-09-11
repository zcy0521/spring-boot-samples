package com.sample.springboot.data.mybatis.mapper.base;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * BaseMapper
 *
 * https://github.com/abel533/Mapper/wiki
 */
public interface BaseMapper<T> extends Mapper<T>, IdsMapper<T> {
}
