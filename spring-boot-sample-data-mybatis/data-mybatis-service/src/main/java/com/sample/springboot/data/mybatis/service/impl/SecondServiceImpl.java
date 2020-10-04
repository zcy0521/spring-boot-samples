package com.sample.springboot.data.mybatis.service.impl;

import com.sample.springboot.data.mybatis.domain.second.SecondDO;
import com.sample.springboot.data.mybatis.mapper.second.SecondMapper;
import com.sample.springboot.data.mybatis.page.Page;
import com.sample.springboot.data.mybatis.query.SecondQuery;
import com.sample.springboot.data.mybatis.service.SecondService;
import com.sample.springboot.data.mybatis.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecondServiceImpl extends BaseServiceImpl<SecondDO> implements SecondService {

    private SecondMapper secondMapper;

    /**
     * 注入secondaryMapper(setter注入)
     * JSR-330写法 @Inject @Named("beanName")
     * Spring写法 @Autowired @Qualifier("beanName")
     */
    @Autowired
    public void setSecondaryMapper(SecondMapper secondMapper) {
        // 将"secondaryMapper"注入BaseServiceImpl中
        super.setMapper(secondMapper);
        // 将"secondaryMapper"注入SecondaryServiceImpl中
        this.secondMapper = secondMapper;
    }

    @Override
    public List<SecondDO> findAll(SecondQuery query) {
        Example example = Example.builder(SecondDO.class)
                .where(buildWhereSqls(query))
                .orderByAsc("id")
                .build();
        return secondMapper.selectByExample(example);
    }

    @Override
    public List<SecondDO> findAll(SecondQuery query, int number, int size) {
        // 分页查询
        startPage(number, size);
        Example example = Example.builder(SecondDO.class)
                .where(buildWhereSqls(query))
                .orderByAsc("id")
                .build();
        List<SecondDO> seconds = secondMapper.selectByExample(example);

        // 分页对象
        int totalElements = secondMapper.selectCountByExample(example);
        Page page = new Page(number, size, totalElements);
        query.setPage(page);

        return seconds;
    }


    /**
     * 根据查询对象生成查询条件
     *
     * @param query 查询对象
     * @return {@link WeekendSqls}
     */
    private WeekendSqls<SecondDO> buildWhereSqls(SecondQuery query) {
        WeekendSqls<SecondDO> sqls = WeekendSqls.custom();
        // 无查询条件
        if (null == query) {
            return sqls;
        }
        // String
        if(null != query.getSampleString()){
            sqls.andLike(SecondDO::getSampleString, "%".concat(query.getSampleString()).concat("%"));
        }
        // Amount
        if(null != query.getMinAmount()){
            sqls.andGreaterThan(SecondDO::getSampleAmount, query.getMinAmount());
        }
        if(null != query.getMaxAmount()){
            sqls.andLessThan(SecondDO::getSampleAmount, query.getMaxAmount());
        }
        // Date
        if(null != query.getMinDate()){
            sqls.andGreaterThan(SecondDO::getSampleDate, query.getMinDate());
        }
        if(null != query.getMaxDate()){
            sqls.andLessThan(SecondDO::getSampleDate, query.getMaxDate());
        }
        // DateTime
        if(null != query.getMinDateTime()){
            sqls.andGreaterThan(SecondDO::getSampleDateTime, query.getMinDateTime());
        }
        if(null != query.getMaxDateTime()){
            sqls.andLessThan(SecondDO::getSampleDateTime, query.getMaxDateTime());
        }
        // Enums
        if(null != query.getSampleEnums() && query.getSampleEnums().length > 0){
            sqls.andIn(SecondDO::getSampleEnum, Arrays.stream(query.getSampleEnums()).collect(Collectors.toSet()));
        }
        // Disable
        if(null != query.getDisable() && query.getDisable()){
            sqls.andEqualTo(SecondDO::getDisabled, 1);
        } else {
            sqls.andEqualTo(SecondDO::getDisabled, 0);
        }
        return sqls;
    }

}
