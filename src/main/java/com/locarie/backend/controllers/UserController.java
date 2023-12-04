package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
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
            @RequestPart("avatar") MultipartFile avatar) {
        UserDto savedUser = service.register(dto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto dto) {
        String token = service.login(dto);
        if (token.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(token, HttpStatus.OK);
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
