package com.sample.springboot.cache.redis.page;

import com.github.pagehelper.PageHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Page {

    /**
     * 默认查询页数
     */
    private static final int DEFAULT_NUMBER = 1;

    /**
     * 默认查询数据量
     */
    private static final int DEFAULT_SIZE = 10;

    /**
     * 最大查询数据量
     */
    private static final int MAX_SIZE = 100;

    /**
     * 当前页
     */
    private int number;

    /**
     * 每页数据量
     */
    private int size;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总数据量
     */
    private int totalElements;

    /**
     * @param number 当前页
     * @param size 每页数据量
     * @param totalElements 总数据量
     */
    @Builder
    public Page(int number, int size, int totalElements) {
        // 当前页
        if (number < 1) {
            number = DEFAULT_NUMBER;
            log.warn("PageNumber less than 1, use default pageNumber: {}", DEFAULT_NUMBER);
        }

        // 每页数据量
        if (size < 1) {
            size = DEFAULT_SIZE;
            log.warn("PageSize less than 1, use default pageSize: {}", DEFAULT_SIZE);
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
            log.warn("PageSize more than {}, use max pageSize: {}", size, MAX_SIZE);
        }

        // 总页数
        int totalPages = (totalElements + size - 1) / size;
        if (number > totalPages) {
            number = totalPages;
        }

        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    /**
     * 开启分页
     */
    public void startPage() {
        PageHelper.startPage(this.number, this.size);
    }

}

