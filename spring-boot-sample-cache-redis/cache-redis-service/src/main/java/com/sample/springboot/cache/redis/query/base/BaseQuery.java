package com.sample.springboot.cache.redis.query.base;

import com.sample.springboot.cache.redis.page.Page;
import lombok.Data;

@Data
public class BaseQuery {

    /**
     * 查询第几页
     */
    private int number;

    /**
     * 查询每页数量
     */
    private int size;

    /**
     * 查询 删除 or 未删除 记录
     */
    private Boolean deleted;

    /**
     * 分页查询结果
     */
    private Page page;

}
