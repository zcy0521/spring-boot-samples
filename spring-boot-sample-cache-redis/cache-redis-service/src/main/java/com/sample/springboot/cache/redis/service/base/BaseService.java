package com.sample.springboot.cache.redis.service.base;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseService {

    /**
     * 默认显示第1页
     */
    private static final int PAGE_NUMBER = 0;

    /**
     * 默认每页显示10条
     */
    private static final int PAGE_SIZE = 10;

    /**
     * 开启分页
     *
     * https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md
     *
     * @param pageNumber 当前显示第几页 1 2 3 ...
     * @param pageSize 当前页显示几条 1 2 3 ...
     */
    protected final void startPage(int pageNumber, int pageSize) {
        // 处理page
        if (pageNumber < 1) {
            pageNumber = PAGE_NUMBER;
            log.warn("PageNumber less than 1, use default pageNumber: {}", PAGE_NUMBER);
        }
        // 处理size
        if (pageSize < 1) {
            pageSize = PAGE_SIZE;
            log.warn("PageSize less than 1, use default pageSize: {}", PAGE_SIZE);
        }
        if (pageSize > PAGE_SIZE) {
            pageSize = PAGE_SIZE;
            log.warn("PageSize more than 1, use default pageSize: {}", PAGE_SIZE);
        }
        PageHelper.startPage(pageNumber, pageSize);
    }

}
