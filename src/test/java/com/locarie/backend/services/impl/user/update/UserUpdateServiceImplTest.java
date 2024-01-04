package com.locarie.backend.services.impl.user.update;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserUpdateDtoCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.user.UserUpdateServiceImpl;
import jakarta.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Transactional
public class UserUpdateServiceImplTest {
  @Autowired private UserUpdateServiceImpl underTests;
  @Autowired private UserRepository userRepository;
  @Autowired private Mapper<UserEntity, UserDto> mapper;

  @Test
  void testUpdatePlainUserShouldSucceed() {
    UserEntity userEntity = givenPlainUserEntity();
    UserUpdateDto userUpdateDto = givenPlainUserUpdateDto();
    UserDto userDto = whenUpdateUserAfterCreated(userEntity, userUpdateDto);
    Optional<UserEntity> updatedUserEntity = whenGetUpdatedUserEntity(userDto.getId());
    assertThat(updatedUserEntity.isPresent()).isTrue();
    thenUpdateResultShouldEqualToUserEntity(userDto, updatedUserEntity.get());
  }

  @Test
  void testUpdateBusinessUserShouldSucceed() {
    UserEntity userEntity = givenBusinessUserEntity();
    UserUpdateDto userUpdateDto = givenBusinessUserUpdateDto();
    UserDto userDto = whenUpdateUserAfterCreated(userEntity, userUpdateDto);
    Optional<UserEntity> updatedUserEntity = whenGetUpdatedUserEntity(userDto.getId());
    assertThat(updatedUserEntity.isPresent()).isTrue();
    thenUpdateResultShouldEqualToUserEntity(userDto, updatedUserEntity.get());
  }

  private UserEntity givenPlainUserEntity() {
    return UserEntityCreator.plainUserEntity();
  }

  private UserUpdateDto givenPlainUserUpdateDto() {
    return UserUpdateDtoCreator.plainUserUpdateDto();
  }

  private UserEntity givenBusinessUserEntity() {
    return UserEntityCreator.businessUserEntityJoleneHornsey();
  }

  private UserUpdateDto givenBusinessUserUpdateDto() {
    return UserUpdateDtoCreator.businessUserUpdateDto();
  }

  private UserDto whenUpdateUserAfterCreated(UserEntity userEntity, UserUpdateDto userUpdateDto) {
    createUser(userEntity);
    return underTests.updateUser(userUpdateDto);
  }

  private Optional<UserEntity> whenGetUpdatedUserEntity(Long id) {
    return userRepository.findById(id);
  }

  private void thenUpdateResultShouldEqualToUserEntity(UserDto result, UserEntity userEntity) {
    UserDto userDto = mapper.mapTo(userEntity);
    assertThat(result)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(userDto);
  }

  private void createUser(UserEntity userEntity) {
    userRepository.save(userEntity);
  }
}
