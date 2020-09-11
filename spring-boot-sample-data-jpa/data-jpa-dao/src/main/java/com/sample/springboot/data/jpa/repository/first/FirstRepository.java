package com.sample.springboot.data.jpa.repository.first;

import com.sample.springboot.data.jpa.domain.first.FirstDO;
import com.sample.springboot.data.jpa.repository.base.BaseRepository;
import com.sample.springboot.data.jpa.repository.first.customer.CustomizedFirstRepository;

public interface FirstRepository extends BaseRepository<FirstDO, Long>, CustomizedFirstRepository {
}
