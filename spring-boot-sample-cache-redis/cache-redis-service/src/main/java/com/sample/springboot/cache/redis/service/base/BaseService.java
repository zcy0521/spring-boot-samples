package com.sample.springboot.cache.redis.service.base;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseService {

    /**
     * 默认显示第1页
     */
    private static final int NUMBER = 0;

    /**
     * 默认每页显示10条
     */
    private static final int SIZE = 10;

    /**
     * IN 查询分区大小
     */
    protected static final int PARTITION_SIZE = 100;

    /**
     * 开启分页
     * @param number 当前页
     * @param size 每页数据量
     */
    protected final void startPage(int number, int size) {
        // 处理page
        if (number < 1) {
            number = NUMBER;
            log.warn("PageNumber less than 1, use default pageNumber: {}", NUMBER);
        }
        // 处理size
        if (size < 1) {
            size = SIZE;
            log.warn("PageSize less than 1, use default pageSize: {}", SIZE);
        }
        if (size > SIZE) {
            size = SIZE;
            log.warn("PageSize more than {}, use default pageSize: {}", SIZE, SIZE);
        }
        PageHelper.startPage(number, size);
    }

}
