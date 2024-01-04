package com.locarie.backend.controllers.user.update;

import com.locarie.backend.domain.dto.user.EmptyUserDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;
import com.locarie.backend.services.user.UserUpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserUpdateController {
  private final UserUpdateService service;

  public UserUpdateController(@Qualifier("UserUpdateService") UserUpdateService service) {
    this.service = service;
  }

  @PostMapping("/{id}")
  ResponseEntity<UserDto> update(@PathVariable("id") Long id, @Valid UserUpdateDto dto) {
    UserDto userDto = service.updateUser(id, dto);
    if (userDto instanceof EmptyUserDto) {
      return new ResponseEntity<>(userDto, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }
}
