package com.locarie.backend.services.impl.user.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserGetServiceImplTest {
  @Autowired private UserServiceImpl underTests;
  @Autowired private UserRepository userRepository;
  @Autowired private Mapper<UserEntity, UserRegistrationDto> mapper;

  @Test
  void testGetShouldReturnUser() {
    UserEntity userEntity = givenUserEntityAfterCreated();
    UserRegistrationDto userRegistrationDto = givenUserRegistrationDto(userEntity);
    Optional<UserDto> result = whenGetUser(userEntity.getId());
    assertThat(result.isPresent()).isTrue();
    thenGetResultShouldEqualToRegistrationDto(result.get(), userRegistrationDto);
  }

  private UserEntity givenUserEntityAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return userRepository.save(userEntity);
  }

  private UserRegistrationDto givenUserRegistrationDto(UserEntity userEntity) {
    return mapper.mapTo(userEntity);
  }

  private Optional<UserDto> whenGetUser(Long id) {
    return underTests.get(id);
  }

  private void thenGetResultShouldEqualToRegistrationDto(
      UserDto result, UserRegistrationDto userRegistrationDto) {
    assertThat(result)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(userRegistrationDto);
  }
}
