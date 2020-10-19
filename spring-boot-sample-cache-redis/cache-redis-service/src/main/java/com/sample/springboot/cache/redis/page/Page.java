package com.sample.springboot.cache.redis.page;

import lombok.Data;

@Data
public class Page {

    /**
     * 默认每页显示数据
     */
    private static final int DEFAULT_SIZE = 10;

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
    public Page(Integer number, Integer size, Integer totalElements) {
        // 当前页
        if (number < 1) {
            number = 1;
        }

        // 每页数据量
        if (size < 1) {
            size = DEFAULT_SIZE;
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

}
