package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.user.UserUpdateService;
import org.springframework.stereotype.Service;

@Service("UserUpdateService")
public class UserUpdateServiceImpl implements UserUpdateService {
  private final UserRepository userRepository;
  private final Mapper<UserEntity, UserDto> userDtoMapper;
  private final Mapper<UserEntity, UserUpdateDto> userUpdateDtoMapper;

  public UserUpdateServiceImpl(UserRepository userRepository, Mapper<UserEntity, UserDto> userDtoMapper, Mapper<UserEntity, UserUpdateDto> userUpdateDtoMapper) {
    this.userRepository = userRepository;
    this.userDtoMapper = userDtoMapper;
    this.userUpdateDtoMapper = userUpdateDtoMapper;
  }

  @Override
  public UserDto updateUser(UserUpdateDto dto) {
    UserEntity userEntity = userUpdateDtoToUserEntity(dto);
    UserEntity updatedEntity = updateUserEntity(userEntity);
    return userEntityToUserDto(updatedEntity);
  }

  private UserEntity userUpdateDtoToUserEntity(UserUpdateDto dto) {
    return userUpdateDtoMapper.mapFrom(dto);
  }

  private UserEntity updateUserEntity(UserEntity entity) {
    return userRepository.save(entity);
  }

  private UserDto userEntityToUserDto(UserEntity entity) {
    return userDtoMapper.mapTo(entity);
  }
}
