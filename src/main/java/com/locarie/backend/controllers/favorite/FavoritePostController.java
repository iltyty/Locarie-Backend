package com.locarie.backend.controllers.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.services.favorite.FavoritePostService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FavoritePostController {
  private final FavoritePostService service;

  public FavoritePostController(FavoritePostService service) {
    this.service = service;
  }

  @PostMapping("/favorite")
  public ResponseEntity<Boolean> favoritePost(
      @RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
    service.favoritePost(userId, postId);
    return new ResponseEntity<>(true, HttpStatus.CREATED);
  }

  @PostMapping("/unfavorite")
  public ResponseEntity<Boolean> unfavoritePost(
      @RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
    service.unfavoritePost(userId, postId);
    return new ResponseEntity<>(true, HttpStatus.CREATED);
  }

  @GetMapping("/favorite")
  public List<PostDto> listFavoritePosts(@RequestParam("userId") Long userId) {
    return service.listFavoritePosts(userId);
  }
}
