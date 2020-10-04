package com.sample.springboot.data.mybatis.service.base.impl;

import com.github.pagehelper.PageHelper;
import com.sample.springboot.data.mybatis.mapper.base.BaseMapper;
import com.sample.springboot.data.mybatis.service.base.BaseService;

import java.util.List;
import java.util.Set;

public class BaseServiceImpl<T> implements BaseService<T> {

    /**
     * 默认显示第1页
     */
    private static final int NUMBER = 0;

    /**
     * 默认每页显示10条
     */
    private static final int SIZE = 10;

    private BaseMapper<T> mapper;

    public void setMapper(BaseMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> findAll(int pageNumber, int pageSize) {
        startPage(pageNumber, pageSize);
        return mapper.selectAll();
    }

    @Override
    public T findById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findAllById(Set<Object> ids) {
        return null;
    }

    @Override
    public T findOne(T entity) {
        return mapper.selectOne(entity);
    }

    @Override
    public List<T> findAll(T entity) {
        return mapper.select(entity);
    }

    @Override
    public List<T> findAll(T entity, int pageNumber, int pageSize) {
        startPage(pageNumber, pageSize);
        return mapper.select(entity);
    }

    @Override
    public int insert(T entity) {
        return mapper.insertSelective(entity);
    }

    @Override
    public boolean update(T entity) {
        int count = mapper.updateByPrimaryKeySelective(entity);
        return count > 0;
    }

    @Override
    public boolean deleteById(Object id) {
        int count = mapper.deleteByPrimaryKey(id);
        return count > 0;
    }

    @Override
    public boolean delete(T entity) {
        int count = mapper.delete(entity);
        return count > 0;
    }

    @Override
    public boolean existsById(Object id) {
        return mapper.existsWithPrimaryKey(id);
    }

    @Override
    public long count(T entity) {
        return mapper.selectCount(entity);
    }

    /**
     * 开启分页
     * @param number 当前页
     * @param size 每页数据量
     */
    protected final void startPage(int number, int size) {
        // 处理page
        if (number < 1) {
            number = NUMBER;
        }
        // 处理size
        if (size < 1) {
            size = SIZE;
        }
        if (size > SIZE) {
            size = SIZE;
        }
        PageHelper.startPage(number, size);
    }

}
