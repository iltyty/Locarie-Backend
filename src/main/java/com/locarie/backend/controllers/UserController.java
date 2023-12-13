package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.*;
import com.locarie.backend.exceptions.UserAlreadyExistsException;
import com.locarie.backend.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(
            @Valid @RequestPart("user") UserRegistrationDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar)
            throws UserAlreadyExistsException {
        UserDto savedUser = service.register(dto, avatar);
        if (savedUser == null) {
            throw new UserAlreadyExistsException("user already exists");
        }
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseDto<UserLoginResponseDto> login(UserLoginRequestDto dto) {
        UserLoginResponseDto result = service.login(dto);
        return result == null
                ? ResponseDto.fail("incorrect email or password")
                : ResponseDto.success(result);
    }

    @GetMapping
    public List<UserDto> listUsers() {
        return service.listUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        return service.getUser(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
