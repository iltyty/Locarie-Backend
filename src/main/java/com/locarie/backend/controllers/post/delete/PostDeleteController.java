package com.locarie.backend.controllers.post.delete;

import com.locarie.backend.services.post.PostDeleteService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostDeleteController {
  private final PostDeleteService service;

  public PostDeleteController(PostDeleteService service) {
    this.service = service;
  }

  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable("id") Long postId) {
    return service.delete(postId);
  }
}
