package com.locarie.backend.controllers.favorite;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.services.favorite.FavoriteBusinessService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class FavoriteBusinessController {
  private final FavoriteBusinessService service;

  public FavoriteBusinessController(FavoriteBusinessService service) {
    this.service = service;
  }

  @PostMapping("/favorite")
  public ResponseEntity<Boolean> favoriteBusiness(
      @RequestParam("userId") Long userId, @RequestParam("businessId") Long businessId) {
    service.favoriteBusiness(userId, businessId);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  @PostMapping("/unfavorite")
  public ResponseEntity<Boolean> unfavoriteBusiness(
      @RequestParam("userId") Long userId, @RequestParam("businessId") Long businessId) {
    service.unfavoriteBusiness(userId, businessId);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  @GetMapping("/favorite")
  public List<UserDto> listFavoriteBusinesses(@RequestParam("userId") Long userId) {
    return service.listFavoriteBusinesses(userId);
  }

  @GetMapping("/favored-by")
  public List<UserDto> listFavoredBy(@RequestParam("businessId") Long businessId) {
    return service.listFavoredBy(businessId);
  }
}
