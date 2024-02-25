package com.locarie.backend.services.impl.user.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.BusinessNameAvatarUrlDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.impl.UserListServiceImpl;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserListServiceImplTest {
  @Autowired private UserListServiceImpl underTests;
  @Autowired private UserRepository userRepository;
  @Autowired private Mapper<UserEntity, UserRegistrationDto> mapper;

  @Test
  void testListAfterRegisteredShouldReturnAllUsers() {
    List<UserEntity> entities = givenUserEntitiesAfterCreated();
    List<UserRegistrationDto> dtos = givenUserRegistrationDtos(entities);
    List<UserDto> result = whenListUsers();
    thenListResultShouldContainsAllDtos(result, dtos);
  }

  @Test
  void testListBusinessesAfterRegisteredShouldReturnCorrectResult() {
    List<UserEntity> entities = givenUserEntitiesAfterCreated();
    List<UserRegistrationDto> dtos = givenUserRegistrationDtos(entities);
    List<BusinessNameAvatarUrlDto> result = whenListBusinesses();
    thenListBusinessesResultShouldBeExact(result, dtos.subList(1, 3));
  }

  private List<UserEntity> givenUserEntitiesAfterCreated() {
    List<UserEntity> userEntities =
        Arrays.asList(
            UserEntityCreator.plainUserEntity(),
            UserEntityCreator.businessUserEntityShreeji(),
            UserEntityCreator.businessUserEntityJoleneHornsey());
    return (List<UserEntity>) userRepository.saveAll(userEntities);
  }

  private List<UserRegistrationDto> givenUserRegistrationDtos(List<UserEntity> userEntities) {
    return userEntities.stream().map(mapper::mapTo).toList();
  }

  private List<UserDto> whenListUsers() {
    return underTests.list();
  }

  private List<BusinessNameAvatarUrlDto> whenListBusinesses() {
    return underTests.listBusinesses();
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

  private void thenListBusinessesResultShouldBeExact(
      List<BusinessNameAvatarUrlDto> result, List<UserRegistrationDto> dtos) {
    assertThat(result.size()).isEqualTo(dtos.size());
    for (int i = 0; i < result.size(); i++) {
      BusinessNameAvatarUrlDto actual = result.get(i);
      UserRegistrationDto expected = dtos.get(i);
      assertThat(actual.getId()).isEqualTo(expected.getId());
      assertThat(actual.getAvatarUrl()).isEqualTo(expected.getAvatarUrl());
      assertThat(actual.getBusinessName()).isEqualTo(expected.getBusinessName());
    }
  }
}
