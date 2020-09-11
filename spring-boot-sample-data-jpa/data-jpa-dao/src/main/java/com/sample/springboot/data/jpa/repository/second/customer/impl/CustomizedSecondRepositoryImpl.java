package com.sample.springboot.data.jpa.repository.second.customer.impl;

import com.sample.springboot.data.jpa.domain.second.SecondDO;
import com.sample.springboot.data.jpa.repository.second.customer.CustomizedSecondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CustomizedSecondRepositoryImpl implements CustomizedSecondRepository {

    @Autowired
    @Qualifier("secondEntityManager")
    private EntityManager entityManager;

    /**
     * Secondary接口自定义方法实现
     */
    @Override
    public SecondDO searchById(Long id) {
        String sql = "SELECT s FROM SecondDO s WHERE s.id = :id";
        TypedQuery<SecondDO> query = entityManager.createQuery(sql, SecondDO.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

}
