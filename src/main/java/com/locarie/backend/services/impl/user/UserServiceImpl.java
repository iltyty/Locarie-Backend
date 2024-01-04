package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.user.*;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.user.UserAvatarService;
import com.locarie.backend.services.user.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {
  private final UserAvatarService avatarService;

  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserServiceImpl(
      @Qualifier("UserAvatarService") UserAvatarService avatarService,
      UserRepository repository,
      Mapper<UserEntity, UserDto> mapper) {
    this.avatarService = avatarService;
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<UserDto> list() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(mapper::mapTo)
        .toList();
  }

  @Override
  public Optional<UserDto> get(Long id) {
    Optional<UserEntity> result = repository.findById(id);
    return result.map(mapper::mapTo);
  }

  @Override
  public UserDto updateAvatar(Long userId, MultipartFile avatar) {
    return avatarService.updateAvatar(userId, avatar);
  }
}
