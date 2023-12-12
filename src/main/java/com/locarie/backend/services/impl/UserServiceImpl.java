package com.locarie.backend.services.impl;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.dto.UserLoginResponseDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.UserService;
import com.locarie.backend.storage.StorageService;
import com.locarie.backend.util.JwtUtil;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;
    private final Mapper<UserEntity, UserDto> mapper;

    private final StorageService storageService;

    public UserServiceImpl(
            JwtUtil jwtUtil,
            UserRepository repository,
            Mapper<UserEntity, UserDto> mapper,
            StorageService storageService) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
        this.mapper = mapper;
        this.storageService = storageService;
    }

    @Override
    public UserDto register(UserRegistrationDto dto, MultipartFile avatar) {
        UserEntity user = mapper.mapFrom(dto);
        UserEntity savedUser = repository.save(user);
        if (avatar == null) {
            return mapper.mapTo(savedUser);
        }
        Path avatarPath =
                storageService.store(avatar, String.format("user_%d/avatar", savedUser.getId()));
        savedUser.setAvatarUrl(avatarPath.toString());
        repository.save(savedUser);
        return mapper.mapTo(savedUser);
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto dto) {
        Optional<UserEntity> user = repository.emailEquals(dto.getEmail());
        if (user.isEmpty()) {
            return null;
        }
        UserEntity result = user.get();
        return new UserLoginResponseDto(
                result.getId(), result.getUsername(), jwtUtil.generateToken(result));
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
