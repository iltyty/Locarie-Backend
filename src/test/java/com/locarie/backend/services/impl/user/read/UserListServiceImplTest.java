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
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserListServiceImplTest {
  @Autowired private UserServiceImpl underTests;
  @Autowired private UserRepository userRepository;
  @Autowired private Mapper<UserEntity, UserRegistrationDto> mapper;

  @Test
  void testListAfterRegisteredShouldReturnAllUsers() {
    List<UserEntity> userEntities = givenUserEntitiesAfterCreated();
    List<UserRegistrationDto> userRegistrationDtos = givenUserRegistrationDtos(userEntities);
    List<UserDto> result = whenListUsers();
    thenListResultShouldContainsAllDtos(result, userRegistrationDtos);
  }

  private List<UserEntity> givenUserEntitiesAfterCreated() {
    List<UserEntity> userEntities =
        Arrays.asList(
            UserEntityCreator.plainUserEntity(),
            UserEntityCreator.businessUserEntityJoleneHornsey());
    return (List<UserEntity>) userRepository.saveAll(userEntities);
  }

  private List<UserRegistrationDto> givenUserRegistrationDtos(List<UserEntity> userEntities) {
    return userEntities.stream().map(mapper::mapTo).toList();
  }

  private List<UserDto> whenListUsers() {
    return underTests.list();
  }

  private void thenListResultShouldContainsAllDtos(
      List<UserDto> result, List<UserRegistrationDto> dtos) {
    assertThat(result.size()).isEqualTo(dtos.size());
    for (int i = 0; i < result.size(); i++) {
      assertThat(result.get(i))
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(dtos.get(i));
    }
  }
}
