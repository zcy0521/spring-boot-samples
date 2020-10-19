package com.sample.springboot.data.mybatis.service.impl;

import com.sample.springboot.data.mybatis.domain.first.FirstDO;
import com.sample.springboot.data.mybatis.mapper.first.FirstMapper;
import com.sample.springboot.data.mybatis.page.Page;
import com.sample.springboot.data.mybatis.query.FirstQuery;
import com.sample.springboot.data.mybatis.service.FirstService;
import com.sample.springboot.data.mybatis.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FirstServiceImpl extends BaseServiceImpl<FirstDO> implements FirstService {

    private FirstMapper firstMapper;

    /**
     * 注入primaryMapper(setter注入)
     * JSR-330写法 @Inject @Named("beanName")
     * Spring写法 @Autowired @Qualifier("beanName")
     */
    @Autowired
    public void setMapper(FirstMapper firstMapper) {
        // 将"primaryMapper"注入BaseServiceImpl中
        super.setMapper(firstMapper);
        // 将"primaryMapper"注入PrimaryServiceImpl中
        this.firstMapper = firstMapper;
    }

    @Override
    public List<FirstDO> findAll(FirstQuery query) {
        Example example = Example.builder(FirstDO.class)
                .where(buildWhereSqls(query))
                .orderByAsc("id")
                .build();
        return firstMapper.selectByExample(example);
    }

    @Override
    public List<FirstDO> findAll(FirstQuery query, int number, int size) {
        // 分页查询
        startPage(number, size);
        Example example = Example.builder(FirstDO.class)
                .where(buildWhereSqls(query))
                .orderByAsc("id")
                .build();
        List<FirstDO> firsts = firstMapper.selectByExample(example);

        // 分页对象
        int totalElements = firstMapper.selectCountByExample(example);
        Page page = new Page(number, size, totalElements);
        query.setPage(page);

        return firsts;
    }


    /**
     * 根据查询对象生成查询条件
     *
     * @param query 查询对象
     * @return {@link WeekendSqls}
     */
    private WeekendSqls<FirstDO> buildWhereSqls(FirstQuery query) {
        WeekendSqls<FirstDO> sqls = WeekendSqls.custom();
        // 无查询条件
        if (null == query) {
            return sqls;
        }
        // String
        if(null != query.getSampleString()){
            sqls.andLike(FirstDO::getSampleString, "%".concat(query.getSampleString()).concat("%"));
        }
        // Amount
        if(null != query.getMinAmount()){
            sqls.andGreaterThan(FirstDO::getSampleAmount, query.getMinAmount());
        }
        if(null != query.getMaxAmount()){
            sqls.andLessThan(FirstDO::getSampleAmount, query.getMaxAmount());
        }
        // Date
        if(null != query.getMinDate()){
            sqls.andGreaterThan(FirstDO::getSampleDate, query.getMinDate());
        }
        if(null != query.getMaxDate()){
            sqls.andLessThan(FirstDO::getSampleDate, query.getMaxDate());
        }
        // DateTime
        if(null != query.getMinDateTime()){
            sqls.andGreaterThan(FirstDO::getSampleDateTime, query.getMinDateTime());
        }
        if(null != query.getMaxDateTime()){
            sqls.andLessThan(FirstDO::getSampleDateTime, query.getMaxDateTime());
        }
        // Enums
        if(null != query.getSampleEnums() && query.getSampleEnums().length > 0){
            sqls.andIn(FirstDO::getSampleEnum, Arrays.stream(query.getSampleEnums()).collect(Collectors.toSet()));
        }
        // Disable
        if(null != query.getDeleted() && query.getDeleted()){
            sqls.andEqualTo(FirstDO::getDeleted, 1);
        } else {
            sqls.andEqualTo(FirstDO::getDeleted, 0);
        }
        return sqls;
    }

}
