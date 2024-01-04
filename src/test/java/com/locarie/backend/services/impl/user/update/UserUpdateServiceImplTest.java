package com.locarie.backend.services.impl.user.update;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserUpdateDtoCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.impl.user.UserUpdateServiceImpl;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserUpdateServiceImplTest {
  @Autowired private UserUpdateServiceImpl underTests;
  @Autowired private UserRepository userRepository;
  @Autowired private Mapper<UserEntity, UserDto> mapper;

  @Test
  void testUpdatePlainUserShouldSucceed() {
    Long id = givenUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenPlainUserUpdateDto();
    UserDto userDto = whenUpdateUser(id, userUpdateDto);
    Optional<UserEntity> updatedUserEntity = whenGetUpdatedUserEntity(userDto.getId());
    assertThat(updatedUserEntity.isPresent()).isTrue();
    thenUpdateResultShouldEqualToUserEntity(userDto, updatedUserEntity.get());
  }

  @Test
  void testUpdateBusinessUserShouldSucceed() {
    Long id = givenUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenBusinessUserUpdateDto();
    UserDto userDto = whenUpdateUser(id, userUpdateDto);
    Optional<UserEntity> updatedUserEntity = whenGetUpdatedUserEntity(userDto.getId());
    assertThat(updatedUserEntity.isPresent()).isTrue();
    thenUpdateResultShouldEqualToUserEntity(userDto, updatedUserEntity.get());
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return createUser(userEntity);
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

  private UserDto whenUpdateUser(Long id, UserUpdateDto userUpdateDto) {
    return underTests.updateUser(id, userUpdateDto);
  }

  private Optional<UserEntity> whenGetUpdatedUserEntity(Long id) {
    return userRepository.findById(id);
  }

  private void thenUpdateResultShouldEqualToUserEntity(UserDto result, UserEntity userEntity) {
    UserDto userDto = mapper.mapTo(userEntity);
    assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(userDto);
  }

  private Long createUser(UserEntity userEntity) {
    return userRepository.save(userEntity).getId();
  }
}
