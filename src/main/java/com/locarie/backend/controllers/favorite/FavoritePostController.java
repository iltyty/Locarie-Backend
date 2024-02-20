package com.locarie.backend.controllers.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.services.favorite.FavoritePostService;
import java.util.List;
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
  public List<PostDto> listFavoritePosts(@RequestParam("userId") Long userId) {
    return service.listFavoritePosts(userId);
  }

  @GetMapping("/favored-by")
  public List<UserDto> listFavoredByPosts(@RequestParam("postId") Long postId) {
    return service.listFavoredBy(postId);
  }
}
