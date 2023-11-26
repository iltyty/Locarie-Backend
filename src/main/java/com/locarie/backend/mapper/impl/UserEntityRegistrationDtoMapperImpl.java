package com.locarie.backend.mapper.impl;

import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEntityRegistrationDtoMapperImpl implements Mapper<UserEntity, UserRegistrationDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserRegistrationDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserRegistrationDto.class);
    }

    @Override
    public UserEntity mapFrom(UserRegistrationDto userRegistrationDto) {
        return modelMapper.map(userRegistrationDto, UserEntity.class);
    }
}
