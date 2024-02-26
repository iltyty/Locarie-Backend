package com.locarie.backend.controllers.user.delete;

import com.locarie.backend.services.user.UserDeleteService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserDeleteController {
  private final UserDeleteService service;

  public UserDeleteController(UserDeleteService service) {
    this.service = service;
  }

  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable("id") Long id) {
    return service.delete(id);
  }
}
