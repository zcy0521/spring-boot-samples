package com.sample.springboot.cache.redis.orika;

import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.model.DeptDTO;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeptDTOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public DeptDTOMapper(MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    public DeptDO convertTo(DeptDTO dto) {
        return mapperFactory.getMapperFacade().map(dto, DeptDO.class);
    }

    public List<DeptDO> convertToList(List<DeptDTO> dtoList) {
        return mapperFactory.getMapperFacade().mapAsList(dtoList, DeptDO.class);
    }

    public DeptDTO convertFrom(DeptDO dept) {
        return mapperFactory.getMapperFacade().map(dept, DeptDTO.class);
    }

    public List<DeptDTO> convertFromList(List<DeptDO> deptList) {
        return mapperFactory.getMapperFacade().mapAsList(deptList, DeptDTO.class);
    }

}
