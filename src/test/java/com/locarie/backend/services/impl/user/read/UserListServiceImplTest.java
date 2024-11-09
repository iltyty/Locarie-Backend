package com.locarie.backend.services.impl.user.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@Transactional
public class UserListServiceImplTest {
  @Autowired private UserListServiceImpl underTests;
  @Autowired private UserRepository userRepository;
  @Autowired private Mapper<UserEntity, UserRegistrationDto> mapper;

  @Test
  void testListWithPaginationShouldReturnExactPaginationSize() {
    List<UserEntity> entities = givenUserEntitiesAfterCreated();
    List<UserRegistrationDto> dtos = givenUserRegistrationDtos(entities);
    Page<UserDto> result = whenListUsers(0, 2);
    thenListResultShouldContainsAllDtos(result, dtos.subList(0, 2));
    result = whenListUsers(1, 2);
    thenListResultShouldContainsAllDtos(result, dtos.subList(2, 3));
  }

  @Test
  void testListBusinessesWithPagination() {
    List<UserEntity> entities = givenUserEntitiesAfterCreated();
    List<UserRegistrationDto> dtos = givenUserRegistrationDtos(entities);
    Page<UserDto> result = whenListBusinesses(0, 2);
    thenListResultShouldContainsAllDtos(result, dtos.subList(1, 3));
    result = whenListBusinesses(1, 1);
    thenListResultShouldContainsAllDtos(result, dtos.subList(2, 3));
  }

  private List<UserEntity> givenUserEntitiesAfterCreated() {
    List<UserEntity> userEntities =
        Arrays.asList(
            UserEntityCreator.plainUserEntity(),
            UserEntityCreator.businessUserEntityJoleneHornsey(),
            UserEntityCreator.businessUserEntityShreeji());
    return userRepository.saveAll(userEntities);
  }

  private List<UserRegistrationDto> givenUserRegistrationDtos(List<UserEntity> userEntities) {
    return userEntities.stream().map(mapper::mapTo).toList();
  }

  private Page<UserDto> whenListUsers(Integer page, Integer pageSize) {
    return underTests.list(PageRequest.of(page, pageSize));
  }

  private Page<UserDto> whenListBusinesses(Integer page, Integer pageSize) {
    return underTests.listBusinesses(PageRequest.of(page, pageSize));
  }

  private void thenListResultShouldContainsAllDtos(
      Page<UserDto> result, List<UserRegistrationDto> dtos) {
    List<UserDto> user = result.getContent();
    assertThat(user.size()).isEqualTo(dtos.size());
    for (int i = 0; i < user.size(); i++) {
      assertThat(user.get(i).getBusinessName()).isEqualTo(dtos.get(i).getBusinessName());
    }
  }
}
