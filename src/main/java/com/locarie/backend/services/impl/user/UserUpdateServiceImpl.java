package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.user.EmptyUserDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.BusinessHoursEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.RequestArgumentNotValidException;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.businesshours.BusinessHoursRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserUpdateService;
import java.lang.reflect.Field;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateServiceImpl implements UserUpdateService {
  private final UserRepository userRepository;
  private final BusinessHoursRepository businessHoursRepository;
  private final Mapper<UserEntity, UserDto> userDtoMapper;
  private final Mapper<UserEntity, UserUpdateDto> userUpdateDtoMapper;

  public UserUpdateServiceImpl(
      UserRepository userRepository,
      BusinessHoursRepository businessHoursRepository,
      Mapper<UserEntity, UserDto> userDtoMapper,
      Mapper<UserEntity, UserUpdateDto> userUpdateDtoMapper) {
    this.userRepository = userRepository;
    this.businessHoursRepository = businessHoursRepository;
    this.userDtoMapper = userDtoMapper;
    this.userUpdateDtoMapper = userUpdateDtoMapper;
  }

  @Override
  public UserDto updateUser(Long id, UserUpdateDto dto) {
    Optional<UserEntity> optionalUserEntity = findUserById(id);
    if (optionalUserEntity.isEmpty()) {
      return new EmptyUserDto();
    }
    UserEntity userEntity = optionalUserEntity.get();
    deleteBusinessHoursIfDtoContainsNewOnes(userEntity, dto);
    updateUserEntityAccordingToUpdateDto(userEntity, dto);
    UserEntity updatedUserEntity = updateUserEntity(userEntity);
    updateBusinessHoursIfNeeded(updatedUserEntity);
    return userEntityToUserDto(updatedUserEntity);
  }

  private Optional<UserEntity> findUserById(Long id) {
    return userRepository.findById(id);
  }

  private void updateUserEntityAccordingToUpdateDto(UserEntity userEntity, UserUpdateDto dto) {
    UserEntity updateUserEntity = userUpdateDtoMapper.mapFrom(dto);
    for (Field field : updateUserEntity.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        Object value = field.get(updateUserEntity);
        if (value != null) {
          field.set(userEntity, value);
        }
      } catch (IllegalAccessException e) {
        throw new RequestArgumentNotValidException(e.getMessage());
      }
    }
  }

  private void deleteBusinessHoursIfDtoContainsNewOnes(UserEntity userEntity, UserUpdateDto dto) {
    if (userEntity.getType() == UserEntity.Type.PLAIN) {
      return;
    }
    if (dto.getBusinessHours() != null && userEntity.getBusinessHours() != null) {
      businessHoursRepository.deleteAll(userEntity.getBusinessHours());
    }
  }

  private UserEntity updateUserEntity(UserEntity entity) {
    return userRepository.save(entity);
  }

  private void updateBusinessHoursIfNeeded(UserEntity userEntity) {
    if (userEntity.getType() == UserEntity.Type.PLAIN) {
      return;
    }
    for (BusinessHoursEntity businessHoursEntity : userEntity.getBusinessHours()) {
      businessHoursEntity.setUser(userEntity);
    }
    businessHoursRepository.saveAll(userEntity.getBusinessHours());
  }

  private UserDto userEntityToUserDto(UserEntity entity) {
    return userDtoMapper.mapTo(entity);
  }
}
