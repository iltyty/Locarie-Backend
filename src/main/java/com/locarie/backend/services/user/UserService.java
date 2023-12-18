package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.dto.UserLoginResponseDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto register(UserRegistrationDto dto, MultipartFile avatar);

    UserLoginResponseDto login(UserLoginRequestDto dto);

    Optional<UserDto> getUser(Long id);

    List<UserDto> listUsers();
}
