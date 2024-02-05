package com.locarie.backend.controllers.post.create;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.services.post.PostCreateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
public class PostCreateController {
  private final PostCreateService service;

  public PostCreateController(PostCreateService service) {
    this.service = service;
  }


  @PostMapping
  public ResponseEntity<PostDto> create(
      @Valid @RequestPart("post") PostDto dto, @RequestPart("images") MultipartFile[] images) {
    PostDto savedPostDto = service.create(dto, images);
    return new ResponseEntity<>(savedPostDto, HttpStatus.CREATED);
  }
}
