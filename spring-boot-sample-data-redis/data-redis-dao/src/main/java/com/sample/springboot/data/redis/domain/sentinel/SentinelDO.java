package com.sample.springboot.data.redis.domain.sentinel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@RedisHash("sentinel")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SentinelDO {

    @Id
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer sampleInteger;

    private Float sampleFloat;

    private Double sampleDouble;

    private String sampleString;

    private String sampleText;

    private LocalDate sampleDate;

    private LocalTime sampleTime;

    private LocalDateTime sampleDateTime;

    /**
     * 枚举值使用 Enum to Integer
     */
    private Integer sampleEnum;

    /**
     * 金额使用 BigDecimal to String
     */
    private String sampleAmount;

    private Boolean sampleValid;

}
