package com.locarie.backend.services;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto register(UserRegistrationDto dto, MultipartFile avatar);

    String login(UserLoginDto dto);

    Optional<UserDto> getUser(Long id);

    List<UserDto> listUsers();
}
