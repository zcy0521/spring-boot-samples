package com.sample.springboot.view.velocity.domain;

import com.sample.springboot.view.velocity.domain.base.BaseDO;
import com.sample.springboot.view.velocity.enums.FileType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SampleFileDO extends BaseDO {

    private Long sampleId;

    private Long fileId;

    private FileType fileType;

}
