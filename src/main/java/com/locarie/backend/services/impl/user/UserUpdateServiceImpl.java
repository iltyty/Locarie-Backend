package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.user.EmptyUserDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserUpdateService;
import org.springframework.stereotype.Service;

@Service("UserUpdateService")
public class UserUpdateServiceImpl implements UserUpdateService {
  private final UserRepository userRepository;
  private final Mapper<UserEntity, UserDto> userDtoMapper;
  private final Mapper<UserEntity, UserUpdateDto> userUpdateDtoMapper;

  public UserUpdateServiceImpl(
      UserRepository userRepository,
      Mapper<UserEntity, UserDto> userDtoMapper,
      Mapper<UserEntity, UserUpdateDto> userUpdateDtoMapper) {
    this.userRepository = userRepository;
    this.userDtoMapper = userDtoMapper;
    this.userUpdateDtoMapper = userUpdateDtoMapper;
  }

  @Override
  public UserDto updateUser(Long id, UserUpdateDto dto) {
    boolean userExists = checkUserExists(id);
    if (!userExists) {
      return new EmptyUserDto();
    }
    UserEntity userEntity = userUpdateDtoToUserEntity(id, dto);
    UserEntity updatedEntity = updateUserEntity(userEntity);
    return userEntityToUserDto(updatedEntity);
  }

  private boolean checkUserExists(Long id) {
    return userRepository.existsById(id);
  }

  private UserEntity userUpdateDtoToUserEntity(Long id, UserUpdateDto dto) {
    UserEntity userEntity = userUpdateDtoMapper.mapFrom(dto);
    userEntity.setId(id);
    return userEntity;
  }

  private UserEntity updateUserEntity(UserEntity entity) {
    return userRepository.save(entity);
  }

  private UserDto userEntityToUserDto(UserEntity entity) {
    return userDtoMapper.mapTo(entity);
  }
}
