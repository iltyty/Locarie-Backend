package com.locarie.backend.services;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto register(UserRegistrationDto dto, MultipartFile avatar);

    String login(UserLoginDto dto);

    Optional<UserDto> getUser(Long id);

    List<UserDto> listUsers();
}
