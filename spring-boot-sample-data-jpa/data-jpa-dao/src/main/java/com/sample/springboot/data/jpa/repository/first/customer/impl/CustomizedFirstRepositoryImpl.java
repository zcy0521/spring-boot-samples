package com.sample.springboot.data.jpa.repository.first.customer.impl;

import com.sample.springboot.data.jpa.domain.first.FirstDO;
import com.sample.springboot.data.jpa.repository.first.customer.CustomizedFirstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CustomizedFirstRepositoryImpl implements CustomizedFirstRepository {

    @Autowired
    @Qualifier("firstEntityManager")
    private EntityManager entityManager;

    /**
     * Primary接口自定义方法实现
     */
    @Override
    public FirstDO searchById(Long id) {
        String sql = "SELECT f FROM FirstDO f WHERE f.id = :id";
        TypedQuery<FirstDO> query = entityManager.createQuery(sql, FirstDO.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

}
