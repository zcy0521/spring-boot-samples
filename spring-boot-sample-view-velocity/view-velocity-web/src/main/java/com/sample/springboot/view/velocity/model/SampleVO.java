package com.sample.springboot.view.velocity.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
public class SampleVO {

    private Long id;

    private Integer sampleInteger;

    private Float sampleFloat;

    private Double sampleDouble;

    private String sampleString;

    private String sampleAmount;

    private String sampleDate;

    private String sampleDateTime;

    private Integer sampleEnum;

    private String sampleText;

    private List<MultipartFile> images;

    private Map<Long, String> imageFiles;

    private List<MultipartFile> audios;

    private Map<Long, String> audioFiles;

    private List<MultipartFile> videos;

    private Map<Long, String> videoFiles;

    private List<MultipartFile> excels;

    private Map<Long, String> excelFiles;

    private Boolean deleted;

    // query 属性方便在insert/update时携带重定向信息
    private QueryVO query = new QueryVO();

    /**
     * 查询条件
     */
    @Data
    @EqualsAndHashCode
    public static class QueryVO {

        /**
         * 默认查询第1页
         */
        private int number = 1;

        /**
         * 默认每页显示9条
         */
        private int size = 9;

        /**
         * 默认只查询有效记录
         */
        private Boolean deleted = false;

        private Integer sampleInteger;

        private String sampleString;

        private String minAmount;

        private String maxAmount;

        private String minDate;

        private String maxDate;

        private String minDateTime;

        private String maxDateTime;

        private Integer[] sampleEnums;

    }

}
