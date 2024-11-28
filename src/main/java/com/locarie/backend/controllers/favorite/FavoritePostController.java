package com.locarie.backend.controllers.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.services.favorite.FavoritePostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class FavoritePostController {
  private final FavoritePostService service;

  public FavoritePostController(FavoritePostService service) {
    this.service = service;
  }

  @PostMapping("/favorite")
  public ResponseEntity<Boolean> favoritePost(
      @RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
    service.favoritePost(userId, postId);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  @PostMapping("/unfavorite")
  public ResponseEntity<Boolean> unfavoritePost(
      @RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
    service.unfavoritePost(userId, postId);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  @GetMapping("/favorite")
  public Page<PostDto> listFavoritePosts(@RequestParam("userId") Long userId, Pageable pageable) {
    return service.listFavoritePosts(userId, pageable);
  }

  @GetMapping("/favored-by")
  public Page<UserDto> listFavoredByPosts(@RequestParam("postId") Long postId, Pageable pageable) {
    return service.listFavoredBy(postId, pageable);
  }

  @GetMapping("/favorite/count")
  public int countFavoritePosts(@RequestParam("userId") Long userId) {
    return service.countFavoritePosts(userId);
  }

  @GetMapping("/favored-by/count")
  public int countFavoredByPosts(@RequestParam("postId") Long postId) {
    return service.countFavoredBy(postId);
  }

  @GetMapping("/is-favored-by")
  public boolean isFavoredBy(
      @RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
    return service.isFavoredBy(userId, postId);
  }
}
