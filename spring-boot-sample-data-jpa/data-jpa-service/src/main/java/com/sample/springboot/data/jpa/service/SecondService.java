package com.sample.springboot.data.jpa.service;

import com.sample.springboot.data.jpa.domain.second.SecondDO;
import com.sample.springboot.data.jpa.service.base.BaseService;

public interface SecondService extends BaseService<SecondDO, Long> {

    SecondDO searchById(Long id);

}
