package com.locarie.backend.services.impl.user.create;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.services.impl.user.UserServiceImpl;
import com.locarie.backend.utils.user.UserRegistrationDtoCreator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserRegisterServiceImplTest {
  @Autowired private UserServiceImpl underTests;

  @Test
  void testPlainUserRegister() {
    UserRegistrationDto userRegistrationDto = givenPlainUserRegistrationDto();
    UserDto userDto = whenRegisterUser(userRegistrationDto);
    thenRegisterResultShouldEqualToRegistrationDto(userDto, userRegistrationDto);
  }

  @Test
  void testBusinessUserRegister() {
    UserRegistrationDto userRegistrationDto = givenBusinessUserRegistrationDto();
    UserDto userDto = whenRegisterUser(userRegistrationDto);
    thenRegisterResultShouldEqualToRegistrationDto(userDto, userRegistrationDto);
  }

  private UserRegistrationDto givenPlainUserRegistrationDto() {
    return UserRegistrationDtoCreator.plainUserRegistrationDto();
  }

  private UserRegistrationDto givenBusinessUserRegistrationDto() {
    return UserRegistrationDtoCreator.businessUserRegistrationDtoJoleneHornsey();
  }

  private UserDto whenRegisterUser(UserRegistrationDto userRegistrationDto) {
    return underTests.register(userRegistrationDto, null);
  }

  private void thenRegisterResultShouldEqualToRegistrationDto(
      UserDto result, UserRegistrationDto userRegistrationDto) {
    userRegistrationDto.setId(result.getId());
    assertThat(result)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(userRegistrationDto);
  }
}
