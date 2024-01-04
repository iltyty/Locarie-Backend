package com.locarie.backend.controllers.user.read;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.services.user.UserGetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserGetController {

  private final UserGetService service;

  public UserGetController(UserGetService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
    return service
        .get(id)
        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
