package com.sample.springboot.cache.redis.orika.mapper;

import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.model.UserDTO;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserDTOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public UserDTOMapper(MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    public UserDO convertTo(UserDTO dto) {
        return mapperFactory.getMapperFacade().map(dto, UserDO.class);
    }

    public List<UserDO> convertToList(List<UserDTO> dtoList) {
        return mapperFactory.getMapperFacade().mapAsList(dtoList, UserDO.class);
    }

    public UserDTO convertFrom(UserDO user) {
        return mapperFactory.getMapperFacade().map(user, UserDTO.class);
    }

    public List<UserDTO> convertFromList(List<UserDO> userList) {
        return mapperFactory.getMapperFacade().mapAsList(userList, UserDTO.class);
    }

}
