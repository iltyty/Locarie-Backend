package com.locarie.backend.controllers.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.services.favorite.FavoriteBusinessService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Page<UserDto> listFavoriteBusinesses(
      @RequestParam("userId") Long userId, Pageable pageable) {
    return service.listFavoriteBusinesses(userId, pageable);
  }

  @GetMapping("/favorite/posts")
  public List<PostDto> getLatestPostsOfFavoriteBusinesses(@RequestParam("userId") Long userId) {
    return service.getLatestPostsOfFavoriteBusinesses(userId);
  }

  @GetMapping("/favored-by")
  public Page<UserDto> listFavoredBy(
      @RequestParam("businessId") Long businessId, Pageable pageable) {
    return service.listFavoredBy(businessId, pageable);
  }

  @GetMapping("/favorite/count")
  public int countFavoriteBusinesses(@RequestParam("userId") Long userId) {
    return service.countFavoriteBusinesses(userId);
  }

  @GetMapping("/favored-by/count")
  public int countFavoredBy(@RequestParam("businessId") Long businessId) {
    return service.countFavoredBy(businessId);
  }

  @GetMapping("/is-favored-by")
  public boolean isFavoredBy(
      @RequestParam("userId") Long userId, @RequestParam("businessId") Long businessId) {
    return service.isFavoredBy(userId, businessId);
  }
}
