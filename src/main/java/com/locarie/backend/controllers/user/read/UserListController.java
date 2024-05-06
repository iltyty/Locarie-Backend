package com.locarie.backend.controllers.user.read;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.services.user.UserListService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserListController {
  private final UserListService service;

  public UserListController(UserListService service) {
    this.service = service;
  }

  @GetMapping
  public List<UserDto> listUsers() {
    return service.list();
  }

  @GetMapping("/businesses")
  public List<UserDto> listBusinesses() {
    return service.listBusinesses();
  }
}
