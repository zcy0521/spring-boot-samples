package com.sample.springboot.alipay.tools;

import org.apache.velocity.tools.config.DefaultKey;

import java.util.stream.IntStream;

@DefaultKey("pagination")
public class PageTool {

    /**
     * 默认显示5页
     */
    private static final int VISIBLE_PAGES = 5;

    /**
     * 获取显示页数范围
     * @param number 当前页
     * @param totalPages 总页数
     * @param visiblePages 显示页数
     */
    public int[] getRange(int number, int totalPages, int visiblePages) {
        if (number < 1) {
            number = 1;
        }
        if (totalPages < 1) {
            totalPages = 1;
        }
        if (visiblePages < 1) {
            visiblePages = VISIBLE_PAGES;
        }
        if (visiblePages > totalPages) {
            visiblePages = totalPages;
        }

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

        return IntStream.rangeClosed(start, end).toArray();
    }

}
