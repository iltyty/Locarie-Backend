package com.locarie.backend.services.impl;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.UserService;
import com.locarie.backend.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;
    private final Mapper<UserEntity, UserDto> mapper;

    public UserServiceImpl(JwtUtil jwtUtil, UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
        this.mapper = mapper;
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

    @Override
    public String login(UserLoginDto dto) {
        Optional<UserEntity> user = repository.emailEquals(dto.getEmail());
        if (user.isEmpty()) {
            return "";
        }
        return jwtUtil.generateToken(user.get());
    }

    @Override
    public List<UserDto> listUsers() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::mapTo)
                .toList();
    }

    @Override
    public Optional<UserDto> getUser(Long id) {
        Optional<UserEntity> result = repository.findById(id);
        return result.map(mapper::mapTo);
    }
}
