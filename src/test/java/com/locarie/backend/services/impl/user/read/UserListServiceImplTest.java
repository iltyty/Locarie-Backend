package com.locarie.backend.services.impl.user.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserLocationDto;
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
  @Autowired private Mapper<UserEntity, UserDto> mapper;

  @Test
  void testListWithPaginationShouldReturnExactPaginationSize() {
    List<UserEntity> entities = givenUserEntitiesAfterCreated();
    List<UserDto> dtos = givenUserDtos(entities);
    Page<UserDto> result = whenListUsers(0, 2);
    thenListResultShouldContainsAllDtos(result, dtos.subList(0, 2));
    result = whenListUsers(1, 1);
    thenListResultShouldContainsAllDtos(result, dtos.subList(1, 2));
  }

  @Test
  void testListBusinessesWithPagination() {
    List<UserEntity> entities = givenUserEntitiesAfterCreated();
    List<UserDto> dtos = givenUserDtos(entities);
    Page<UserDto> result = whenListBusinesses(0, 2);
    thenListResultShouldContainsAllDtos(result, dtos.subList(1, 3));
    result = whenListBusinesses(1, 1);
    thenListResultShouldContainsAllDtos(result, dtos.subList(2, 3));
  }

  @Test
  void testListAllBusinesses() {
    List<UserEntity> entities = givenUserEntitiesAfterCreated();
    List<UserLocationDto> result = whenListAllBusinesses();
    List<UserDto> dtos = givenUserDtos(entities);
    thenListResultShouldContainsAllLocationDtos(result, dtos.subList(1, 3));
  }

  private List<UserEntity> givenUserEntitiesAfterCreated() {
    List<UserEntity> userEntities =
        Arrays.asList(
            UserEntityCreator.plainUserEntity(),
            UserEntityCreator.businessUserEntityJoleneHornsey(),
            UserEntityCreator.businessUserEntityShreeji());
    return userRepository.saveAll(userEntities);
  }

  private List<UserDto> givenUserDtos(List<UserEntity> userEntities) {
    return userEntities.stream().map(mapper::mapTo).toList();
  }

  private Page<UserDto> whenListUsers(Integer page, Integer pageSize) {
    return underTests.list(PageRequest.of(page, pageSize));
  }

  private Page<UserDto> whenListBusinesses(Integer page, Integer pageSize) {
    return underTests.listBusinesses(PageRequest.of(page, pageSize));
  }

  private List<UserLocationDto> whenListAllBusinesses() {
    return underTests.listAllBusinesses();
  }

  private void thenListResultShouldContainsAllDtos(Page<UserDto> result, List<UserDto> dtos) {
    List<UserDto> users = result.getContent();
    assertThat(users.size()).isEqualTo(dtos.size());
    for (int i = 0; i < users.size(); i++) {
      assertThat(users.get(i).getBusinessName()).isEqualTo(dtos.get(i).getBusinessName());
    }
  }

  private void thenListResultShouldContainsAllLocationDtos(
      List<UserLocationDto> result, List<UserDto> dtos) {
    assertThat(result.size()).isEqualTo(dtos.size());
    for (int i = 0; i < result.size(); i++) {
      assertThat(result.get(i).getLocation().getX()).isEqualTo(dtos.get(i).getLocation().getX());
      assertThat(result.get(i).getLocation().getY()).isEqualTo(dtos.get(i).getLocation().getY());
      assertThat(result.get(i).getAvatarUrl()).isEqualTo(dtos.get(i).getAvatarUrl());
    }
  }
}
