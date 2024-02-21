package com.locarie.backend.datacreators.user;

import com.locarie.backend.datacreators.businesshours.BusinessHoursDtoCreator;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.domain.enums.BusinessCategory;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.user.UserEntityUpdateDtoMapperImpl;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserUpdateDtoCreator {
  private static final Mapper<UserEntity, UserUpdateDto> mapper =
      new UserEntityUpdateDtoMapperImpl();

  public static UserUpdateDto fullPlainUserUpdateDto() {
    return UserUpdateDto.builder()
        .email("test@email.com")
        .username("johndoe")
        .firstName("John")
        .lastName("Doe")
        .birthday(Instant.parse("2000-10-21T17:30:00.00Z"))
        .build();
  }

  public static UserUpdateDto partialPlainUserUpdateDto() {
    return UserUpdateDto.builder().username("johndoe").build();
  }

  public static UserUpdateDto fullBusinessUserUpdateDto() {
    return UserUpdateDto.builder()
        .email("test@email.com")
        .username("johndoe")
        .firstName("John")
        .lastName("Doe")
        .birthday(Instant.parse("2000-10-21T17:30:00.00Z"))
        .businessName("Business name")
        .homepageUrl("https://www.homepage.com")
        .categories(new ArrayList<>(
            List.of(BusinessCategory.LIFESTYLE.getValue())
        ))
        .introduction("Introduction")
        .phone("12345678")
        .businessHours(BusinessHoursDtoCreator.businessHoursDtos())
        .build();
  }

  public static UserUpdateDto partialBusinessUserUpdateDto() {
    return UserUpdateDto.builder()
        .businessName("Business name")
        .businessHours(BusinessHoursDtoCreator.randomBusinessHoursDtos())
        .build();
  }

  public static UserUpdateDto randomBusinessHoursUserUpdateDto() {
    return UserUpdateDto.builder()
        .businessHours(BusinessHoursDtoCreator.randomBusinessHoursDtos())
        .build();
  }
}
