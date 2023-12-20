package com.locarie.backend.datacreators.user;

import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.UserEntityRegistrationDtoMapperImpl;

public class UserRegistrationDtoCreator {
  private static final Mapper<UserEntity, UserRegistrationDto> mapper =
      new UserEntityRegistrationDtoMapperImpl();

  public static UserRegistrationDto plainUserRegistrationDto() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return mapper.mapTo(userEntity);
  }

  public static UserRegistrationDto businessUserRegistrationDtoJoleneHornsey() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    return mapper.mapTo(userEntity);
  }

  public static UserRegistrationDto businessUserRegistrationDtoShreeji() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityShreeji();
    return mapper.mapTo(userEntity);
  }
}
