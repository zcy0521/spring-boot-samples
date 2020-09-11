package com.sample.springboot.data.jpa.repository.second.customer;

import com.sample.springboot.data.jpa.domain.second.SecondDO;

public interface CustomizedSecondRepository {

    SecondDO searchById(Long id);

}
