package com.locarie.backend.services.impl.user.update;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.datacreators.user.UserUpdateDtoCreator;
import com.locarie.backend.domain.dto.businesshours.BusinessHoursDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.BusinessHoursEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.businesshours.BusinessHoursRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.impl.user.UserUpdateServiceImpl;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserUpdateServiceImplTest {
  @Autowired private UserUpdateServiceImpl underTests;

  @Autowired private UserRepository userRepository;
  @Autowired private BusinessHoursRepository businessHoursRepository;

  @Autowired private Mapper<UserEntity, UserDto> userMapper;
  @Autowired private Mapper<BusinessHoursEntity, BusinessHoursDto> businessHoursMapper;

  @Test
  void testUpdatePlainUserShouldSucceed() {
    Long id = givenPlainUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenPlainUserUpdateDto();
    UserDto userDto = whenUpdateUser(id, userUpdateDto);
    Optional<UserEntity> updatedUserEntity = whenGetUpdatedUserEntity(userDto.getId());
    assertThat(updatedUserEntity.isPresent()).isTrue();
    thenUpdateResultShouldEqualToUserEntity(userDto, updatedUserEntity.get());
  }

  @Test
  void testUpdateBusinessUserShouldSucceed() {
    Long id = givenPlainUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenBusinessUserUpdateDto();
    UserDto userDto = whenUpdateUser(id, userUpdateDto);
    Optional<UserEntity> updatedUserEntity = whenGetUpdatedUserEntity(userDto.getId());
    assertThat(updatedUserEntity.isPresent()).isTrue();
    thenUpdateResultShouldEqualToUserEntity(userDto, updatedUserEntity.get());
  }

  @Test
  void testUpdateBusinessHoursShouldSucceed() {
    Long id = givenBusinessUserIdAfterCreated();
    UserUpdateDto userUpdateDto = givenUserUpdateDtoWithRandomBusinessHours();
    UserDto userDto = whenUpdateUser(id, userUpdateDto);
    Optional<UserEntity> updatedUserEntity = whenGetUpdatedUserEntity(userDto.getId());
    List<BusinessHoursDto> businessHoursDtos = whenGetUpdatedBusinessHours();
    assertThat(updatedUserEntity.isPresent()).isTrue();
    thenUpdateResultShouldEqualToUserEntity(userDto, updatedUserEntity.get());
    thenUpdateResultShouldContainBusinessHours(userDto, businessHoursDtos);
    thenUpdatedBusinessHoursShouldHaveUser(businessHoursDtos, userDto);
  }

  private Long givenPlainUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.plainUserEntity();
    return createUser(userEntity);
  }

  private Long givenBusinessUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityShreeji();
    return createUser(userEntity);
  }

  private UserUpdateDto givenPlainUserUpdateDto() {
    return UserUpdateDtoCreator.fullPlainUserUpdateDto();
  }

  private UserUpdateDto givenBusinessUserUpdateDto() {
    return UserUpdateDtoCreator.fullBusinessUserUpdateDto();
  }

  private UserUpdateDto givenUserUpdateDtoWithRandomBusinessHours() {
    return UserUpdateDtoCreator.randomBusinessHoursUserUpdateDto();
  }

  private UserDto whenUpdateUser(Long id, UserUpdateDto userUpdateDto) {
    return underTests.updateUser(id, userUpdateDto);
  }

  private Optional<UserEntity> whenGetUpdatedUserEntity(Long id) {
    return userRepository.findById(id);
  }

  private List<BusinessHoursDto> whenGetUpdatedBusinessHours() {
    List<BusinessHoursEntity> businessHoursEntities =
        (List<BusinessHoursEntity>) businessHoursRepository.findAll();
    return businessHoursEntities.stream().map(businessHoursMapper::mapTo).toList();
  }

  private void thenUpdateResultShouldEqualToUserEntity(UserDto result, UserEntity userEntity) {
    UserDto userDto = userMapper.mapTo(userEntity);
    assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(userDto);
  }

  private void thenUpdateResultShouldContainBusinessHours(
      UserDto result, List<BusinessHoursDto> businessHoursDtos) {
    List<BusinessHoursDto> resultBusinessHoursDtos = result.getBusinessHours();
    assertThat(resultBusinessHoursDtos).hasSameSizeAs(businessHoursDtos);
    for (int i = 0; i < resultBusinessHoursDtos.size(); i++) {
      BusinessHoursDto resultBusinessHoursDto = resultBusinessHoursDtos.get(i);
      BusinessHoursDto businessHoursDto = businessHoursDtos.get(i);
      assertThat(resultBusinessHoursDto)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(businessHoursDto);
    }
  }

  private void thenUpdatedBusinessHoursShouldHaveUser(
      List<BusinessHoursDto> businessHoursDtos, UserDto userDto) {
    for (BusinessHoursDto businessHoursDto : businessHoursDtos) {
      UserDto user = businessHoursDto.getUser();
      assertThat(user).isNotNull();
      assertThat(user).usingRecursiveComparison().ignoringFields("id").isEqualTo(userDto);
    }
  }

  private Long createUser(UserEntity userEntity) {
    return userRepository.save(userEntity).getId();
  }
}
