package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserListService;
import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class UserListServiceImpl implements UserListService {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> mapper;

  public UserListServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
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
  public List<UserDto> listBusinesses() {
    return repository.listBusinesses().stream()
        .map(mapper::mapTo)
        .toList();
  }
}
