package com.locarie.backend.controllers.user.read;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserLocationDto;
import com.locarie.backend.services.user.UserListService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserListController {
  private final UserListService service;

  public UserListController(UserListService service) {
    this.service = service;
  }

  @GetMapping
  public Page<UserDto> listUsers(Pageable pageable) {
    return service.list(pageable);
  }

  @GetMapping("/businesses")
  public Page<UserDto> listBusinesses(
      @RequestParam(value = "latitude") double latitude,
      @RequestParam(value = "longitude") double longitude,
      Pageable pageable) {
    return service.listBusinesses(latitude, longitude, pageable);
  }

  @GetMapping("/businesses/all")
  public List<UserLocationDto> listAllBusinesses() {
    return service.listAllBusinesses();
  }
}
