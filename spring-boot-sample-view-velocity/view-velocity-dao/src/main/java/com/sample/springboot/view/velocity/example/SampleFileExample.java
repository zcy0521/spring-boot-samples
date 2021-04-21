package com.sample.springboot.view.velocity.example;

import com.sample.springboot.view.velocity.enums.FileType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SampleFileExample {

    private Long sampleId;

    private Set<Long> sampleIds;

    private Long fileId;

    private Set<Long> fileIds;

    private FileType fileType;

    private FileType[] fileTypes;

    private Boolean deleted;

}
