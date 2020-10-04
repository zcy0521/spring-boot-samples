package com.sample.springboot.data.mybatis.page;

import lombok.Data;

@Data
public class Page {

    /**
     * 默认每页显示数据
     */
    private static final Integer DEFAULT_SIZE = 10;

    /**
     * 默认显示页数
     */
    private static final Integer DEFAULT_VISIBLE = 5;

    /**
     * 当前页
     */
    private Integer number;

    /**
     * 显示起始页
     */
    private Integer startNumber;

    /**
     * 显示最后页
     */
    private Integer endNumber;

    /**
     * 每页数据量
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 总数据量
     */
    private Integer totalElements;

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

        // 显示页数
        int visiblePages = DEFAULT_VISIBLE;
        if (visiblePages > totalPages) {
            visiblePages = totalPages;
        }

        // 起始页码 结束页码
        int half = visiblePages / 2;
        int start = number - half + 1 - (visiblePages % 2);
        int end = number + half;
        if (start < 1) {
            start = 1;
            end = visiblePages;
        }
        if (end > totalPages) {
            end = totalPages;
            start = totalPages - visiblePages + 1;
        }

        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.startNumber = start;
        this.endNumber = end;
    }

}
