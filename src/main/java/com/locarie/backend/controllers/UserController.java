package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegistrationDto dto) {
        UserDto savedUser = service.register(dto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
