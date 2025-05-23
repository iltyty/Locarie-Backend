package com.locarie.backend.datacreators.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.user.UserEntityDtoMapperImpl;

public class UserDtoCreator {
  private static final Mapper<UserEntity, UserDto> mapper = new UserEntityDtoMapperImpl();

  public static UserDto plainUserDto() {
    return mapper.mapTo(UserEntityCreator.plainUserEntity());
  }

  public static UserDto businessUserDtoJoleneHornsey() {
    return mapper.mapTo(UserEntityCreator.businessUserEntityJoleneHornsey());
  }

  public static UserDto businessUserDtoShreeji() {
    return mapper.mapTo(UserEntityCreator.businessUserEntityShreeji());
  }
}
