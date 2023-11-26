package com.locarie.backend.services.impl;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Mapper<UserEntity, UserDto> mapper;
    private final UserRepository repository;


    public UserServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        return repository.save(user);
    }

    @Override
    public UserDto register(UserRegistrationDto dto) {
        UserEntity user = mapper.mapFrom(dto);
        UserEntity savedUser = createUser(user);
        return mapper.mapTo(savedUser);
    }
}
