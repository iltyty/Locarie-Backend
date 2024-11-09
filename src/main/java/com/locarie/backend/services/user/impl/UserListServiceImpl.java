package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserListServiceImpl implements UserListService {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserListServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> list(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::mapTo);
  }

  @Override
  public Page<UserDto> listBusinesses(Pageable pageable) {
    return repository.findByType(UserEntity.Type.BUSINESS, pageable).map(mapper::mapTo);
  }
}
