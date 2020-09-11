package com.sample.springboot.data.jpa.repository.first.customer;

import com.sample.springboot.data.jpa.domain.first.FirstDO;

public interface CustomizedFirstRepository {

    FirstDO searchById(Long id);

}
