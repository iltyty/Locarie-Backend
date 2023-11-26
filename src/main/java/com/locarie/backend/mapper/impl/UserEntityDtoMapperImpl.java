package com.locarie.backend.mapper.impl;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEntityDtoMapperImpl implements Mapper<UserEntity, UserDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserDto mapTo(UserEntity user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
