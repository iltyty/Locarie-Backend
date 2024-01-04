package com.locarie.backend.mapper.impl.user;

import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEntityUpdateDtoMapperImpl implements Mapper<UserEntity, UserUpdateDto> {
  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public UserUpdateDto mapTo(UserEntity userEntity) {
    return modelMapper.map(userEntity, UserUpdateDto.class);
  }

  @Override
  public UserEntity mapFrom(UserUpdateDto userUpdateDto) {
    return modelMapper.map(userUpdateDto, UserEntity.class);
  }
}
