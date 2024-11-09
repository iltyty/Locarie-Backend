package com.locarie.backend.mapper.impl.user;

import com.locarie.backend.domain.dto.user.UserLocationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEntityLocationDtoMapperImpl implements Mapper<UserEntity, UserLocationDto> {
  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public UserLocationDto mapTo(UserEntity user) {
    return modelMapper.map(user, UserLocationDto.class);
  }

  @Override
  public UserEntity mapFrom(UserLocationDto userDto) {
    return modelMapper.map(userDto, UserEntity.class);
  }
}
