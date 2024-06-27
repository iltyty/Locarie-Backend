package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.dto.user.UserLoginRequestDto;
import com.locarie.backend.domain.dto.user.UserLoginResponseDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserLoginService;
import com.locarie.backend.util.JwtUtil;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public UserLoginServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public UserLoginResponseDto login(UserLoginRequestDto dto) {
    Optional<UserEntity> user = userRepository.findByEmail(dto.getEmail());
    if (user.isEmpty()) {
      return null;
    }
    UserEntity result = user.get();
    if (!result.getPassword().equals(dto.getPassword())) {
      return null;
    }
    return UserLoginResponseDto.builder()
        .id(result.getId())
        .email(result.getEmail())
        .type(result.getType().toString())
        .username(result.getUsername())
        .avatarUrl(result.getAvatarUrl())
        .jwtToken(jwtUtil.generateToken(result))
        .build();
  }
}
