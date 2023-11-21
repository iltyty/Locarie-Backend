package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.entities.User;
import com.locarie.backend.mapper.Mapper;
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

    private final Mapper<User, UserDto> mapper;

    public UserController(UserService service, Mapper<User, UserDto> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = mapper.mapFrom(userDto);
        User savedUser = service.createUser(user);
        return new ResponseEntity<>(mapper.mapTo(savedUser), HttpStatus.CREATED);
    }
}
