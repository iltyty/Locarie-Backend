package com.locarie.backend.datacreators.user;

import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.user.UserEntityUpdateDtoMapperImpl;

import java.time.Instant;

public class UserUpdateDtoCreator {
  private static final Mapper<UserEntity, UserUpdateDto> mapper =
      new UserEntityUpdateDtoMapperImpl();

  public static UserUpdateDto plainUserUpdateDto() {
    return UserUpdateDto.builder()
        .email("test@email.com")
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .birthday(Instant.parse("2000-10-21T17:30:00.00Z"))
        .build();
  }

  public static UserUpdateDto businessUserUpdateDto() {
    return UserUpdateDto.builder()
        .email("test@email.com")
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .birthday(Instant.parse("2000-10-21T17:30:00.00Z"))
        .businessName("Business name")
        .homepageUrl("https://www.homepage.com")
        .category("Cafe")
        .introduction("Introduction")
        .phone("12345678")
        .build();
  }
}
