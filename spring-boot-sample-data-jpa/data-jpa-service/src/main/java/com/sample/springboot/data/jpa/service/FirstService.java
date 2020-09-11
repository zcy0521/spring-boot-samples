package com.sample.springboot.data.jpa.service;

import com.sample.springboot.data.jpa.domain.first.FirstDO;
import com.sample.springboot.data.jpa.service.base.BaseService;

public interface FirstService extends BaseService<FirstDO, Long> {

    FirstDO searchById(Long id);

}
