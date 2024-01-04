package com.locarie.backend.services.impl.user.create;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserRegistrationDtoCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.services.impl.user.UserRegisterServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserRegisterServiceImplTest {
  @Autowired private UserRegisterServiceImpl underTests;

  @Test
  void testPlainUserRegisterShouldSucceed() {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    UserDto userDto = whenRegisterUser(userRegistrationDto);
    thenResultShouldEqualToRegistrationDto(userDto, userRegistrationDto);
  }

  @Test
  void testBusinessUserRegisterShouldSucceed() {
    UserRegistrationDto userRegistrationDto = givenBusinessUserRegistrationDto();
    UserDto userDto = whenRegisterUser(userRegistrationDto);
    thenResultShouldEqualToRegistrationDto(userDto, userRegistrationDto);
  }

  private UserRegistrationDto givenPlainUserRegistrationDto() {
    return UserRegistrationDtoCreator.plainUserRegistrationDto();
  }

  private UserRegistrationDto givenBusinessUserRegistrationDto() {
    return UserRegistrationDtoCreator.businessUserRegistrationDtoJoleneHornsey();
  }

  private UserDto whenRegisterUser(UserRegistrationDto userRegistrationDto) {
    return underTests.register(userRegistrationDto);
  }

  private void thenResultShouldEqualToRegistrationDto(
      UserDto result, UserRegistrationDto userRegistrationDto) {
    userRegistrationDto.setId(result.getId());
    assertThat(result)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(userRegistrationDto);
  }
}
