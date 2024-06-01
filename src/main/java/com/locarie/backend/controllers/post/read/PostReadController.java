package com.locarie.backend.controllers.post.read;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.services.post.PostReadService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostReadController {
  private final PostReadService service;

  public PostReadController(PostReadService service) {
    this.service = service;
  }

  @GetMapping
  public List<PostDto> list() {
    return service.list();
  }

  @GetMapping("/nearby")
  public List<PostDto> listNearby(
      @RequestParam(value = "latitude") double latitude,
      @RequestParam(value = "longitude") double longitude,
      @RequestParam(value = "distance") double distance) {
    return service.listNearby(latitude, longitude, distance);
  }

  @GetMapping("/nearby-all")
  public List<PostDto> listNearbyAll(
      @RequestParam(value = "latitude") double latitude,
      @RequestParam(value = "longitude") double longitude) {
    return service.listNearbyAll(latitude, longitude);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDto> get(@PathVariable Long id) {
    return service
        .get(id)
        .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/user/{id}")
  public List<PostDto> listUserPosts(@PathVariable("id") Long id) {
    return service.listUserPosts(id);
  }

  @GetMapping("within")
  public List<PostDto> listWithin(
      @RequestParam("minLatitude") double minLatitude,
      @RequestParam("maxLatitude") double maxLatitude,
      @RequestParam("minLongitude") double minLongitude,
      @RequestParam("maxLongitude") double maxLongitude) {
    return service.listWithin(minLatitude, maxLatitude, minLongitude, maxLongitude);
  }
}
