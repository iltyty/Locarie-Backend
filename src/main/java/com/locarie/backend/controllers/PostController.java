package com.locarie.backend.controllers;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.services.post.PostCreateService;
import com.locarie.backend.services.post.PostReadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostCreateService postCreateService;
  private final PostReadService postReadService;

  public PostController(PostCreateService postCreateService, PostReadService postReadService) {
    this.postCreateService = postCreateService;
    this.postReadService = postReadService;
  }

  @PostMapping
  public ResponseEntity<PostDto> create(
      @Valid @RequestPart("post") PostDto dto, @RequestPart("images") MultipartFile[] images) {
    PostDto savedPostDto = postCreateService.create(dto, images);
    return new ResponseEntity<>(savedPostDto, HttpStatus.CREATED);
  }

  @GetMapping
  public List<PostDto> list() {
    return postReadService.list();
  }

  @GetMapping("/nearby")
  public List<PostDto> listNearby(
      @RequestParam(value = "latitude") double latitude,
      @RequestParam(value = "longitude") double longitude,
      @RequestParam(value = "distance") double distance) {
    return postReadService.listNearby(latitude, longitude, distance);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDto> get(@PathVariable Long id) {
    return postReadService
        .get(id)
        .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
