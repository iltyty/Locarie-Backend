package com.locarie.backend.services.impl.user;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.dto.UserLoginResponseDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.user.UserAvatarService;
import com.locarie.backend.services.user.UserService;
import com.locarie.backend.util.JwtUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {
  private final UserAvatarService avatarService;

  private final JwtUtil jwtUtil;
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserServiceImpl(
      @Qualifier("UserAvatarService") UserAvatarService avatarService,
      JwtUtil jwtUtil,
      UserRepository repository,
      Mapper<UserEntity, UserDto> mapper) {
    this.avatarService = avatarService;
    this.jwtUtil = jwtUtil;
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public UserDto register(UserRegistrationDto dto) {
    if (repository.findByEmail(dto.getEmail()).isPresent()) {
      return null;
    }
    UserEntity user = mapper.mapFrom(dto);
    UserEntity savedUser = repository.save(user);
    return mapper.mapTo(savedUser);
  }

  @Override
  public UserLoginResponseDto login(UserLoginRequestDto dto) {
    Optional<UserEntity> user = repository.findByEmail(dto.getEmail());
    if (user.isEmpty()) {
      return null;
    }
    UserEntity result = user.get();
    if (!result.getPassword().equals(dto.getPassword())) {
      return null;
    }
    return UserLoginResponseDto.builder()
        .id(result.getId())
        .type(result.getType().toString())
        .username(result.getUsername())
        .avatarUrl(result.getAvatarUrl())
        .jwtToken(jwtUtil.generateToken(result))
        .build();
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
  public UserDto update(Long userId, MultipartFile avatar) {
    return avatarService.update(userId, avatar);
  }
}
