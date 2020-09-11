package com.sample.springboot.data.jpa.repository.second;

import com.sample.springboot.data.jpa.domain.second.SecondDO;
import com.sample.springboot.data.jpa.repository.base.BaseRepository;
import com.sample.springboot.data.jpa.repository.second.customer.CustomizedSecondRepository;

public interface SecondRepository extends BaseRepository<SecondDO, Long>, CustomizedSecondRepository {
}
