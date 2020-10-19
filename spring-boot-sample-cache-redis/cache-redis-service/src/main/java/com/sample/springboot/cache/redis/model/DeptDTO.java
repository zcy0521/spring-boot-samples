package com.sample.springboot.cache.redis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class DeptDTO {

    private Long id;

    private String deptName;

    private Integer level;

    private DeptDTO parent;

    private List<DeptDTO> children;

    private DeptDTO[] ancestors;

    private List<UserDTO> admins;

}
