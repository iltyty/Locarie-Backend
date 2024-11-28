package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserLocationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserListService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserListServiceImpl implements UserListService {
  private final UserRepository repository;
  private final Mapper<UserEntity, UserDto> entityDtoMapper;
  private final Mapper<UserEntity, UserLocationDto> entityLocationDtoMapper;

  public UserListServiceImpl(
      UserRepository repository,
      Mapper<UserEntity, UserDto> mapper,
      Mapper<UserEntity, UserLocationDto> entityLocationDtoMapper) {
    this.repository = repository;
    this.entityDtoMapper = mapper;
    this.entityLocationDtoMapper = entityLocationDtoMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> list(Pageable pageable) {
    return repository.findAll(pageable).map(entityDtoMapper::mapTo);
  }

  @Override
  public Page<UserDto> listBusinesses(
      double latitude, double longitude, String name, Pageable pageable) {
    return repository
        .listBusinesses(latitude, longitude, "%" + name + "%", pageable)
        .map(entityDtoMapper::mapTo);
  }

  @Override
  public List<UserLocationDto> listAllBusinesses() {
    return repository.findByType(UserEntity.Type.BUSINESS).stream()
        .filter(this::isProfileComplete)
        .map(entityLocationDtoMapper::mapTo)
        .toList();
  }

  private boolean isProfileComplete(UserEntity user) {
    return notEmpty(user.getProfileImageUrls())
        && notEmpty(user.getAvatarUrl())
        && notEmpty(user.getBusinessName())
        && notEmpty(user.getCategories())
        && notEmpty(user.getIntroduction())
        && notEmpty(user.getBusinessHours())
        && user.getLocation() != null;
  }

  private boolean notEmpty(String s) {
    return s != null && !s.isEmpty();
  }

  private boolean notEmpty(List<?> list) {
    return list != null && !list.isEmpty();
  }
}
