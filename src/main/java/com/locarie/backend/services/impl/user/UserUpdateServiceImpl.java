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
    UserEntity oldUserEntity = optionalUserEntity.get();
    UserEntity newUserEntity = userUpdateDtoToEntity(dto);
    UserEntity updatedUserEntity = updateUserEntity(oldUserEntity, newUserEntity);
    return userEntityToUserDto(updatedUserEntity);
  }

  private Optional<UserEntity> findUserById(Long id) {
    return userRepository.findById(id);
  }

  private UserEntity userUpdateDtoToEntity(UserUpdateDto dto) {
    return userUpdateDtoMapper.mapFrom(dto);
  }

  private UserEntity updateUserEntity(UserEntity oldEntity, UserEntity newEntity) {
    updateBusinessHoursIfNeeded(oldEntity, newEntity);
    updateOtherFields(oldEntity, newEntity);
    return saveUserEntity(oldEntity);
  }

  private void updateBusinessHoursIfNeeded(UserEntity oldEntity, UserEntity newEntity) {
    if (oldEntity.getType() == UserEntity.Type.PLAIN) {
      return;
    }
    if (oldEntity.getBusinessHours() == null) {
      oldEntity.setBusinessHours(newEntity.getBusinessHours());
    } else {
      businessHoursRepository.deleteAll(oldEntity.getBusinessHours());
      oldEntity.getBusinessHours().clear();
      oldEntity.getBusinessHours().addAll(newEntity.getBusinessHours());
      for (BusinessHoursEntity businessHoursEntity : oldEntity.getBusinessHours()) {
        businessHoursEntity.setUser(oldEntity);
      }
    }
  }

  private void updateOtherFields(UserEntity oldEntity, UserEntity newEntity) {
    for (Field field : newEntity.getClass().getDeclaredFields()) {
      if (field.getName().equals("businessHours")) {
        continue;
      }
      field.setAccessible(true);
      try {
        Object value = field.get(newEntity);
        if (value != null) {
          field.set(oldEntity, value);
        }
      } catch (IllegalAccessException e) {
        throw new RequestArgumentNotValidException(e.getMessage());
      }
    }
  }

  private UserEntity saveUserEntity(UserEntity userEntity) {
    return userRepository.save(userEntity);
  }

  private UserDto userEntityToUserDto(UserEntity entity) {
    return userDtoMapper.mapTo(entity);
  }
}
