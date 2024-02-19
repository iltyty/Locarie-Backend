package com.locarie.backend.services.utils;

import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.exceptions.PostNotFoundException;
import com.locarie.backend.repositories.post.PostRepository;
import org.springframework.stereotype.Component;

@Component
public class PostFindUtils {
  private final PostRepository repository;

  public PostFindUtils(PostRepository repository) {
    this.repository = repository;
  }

  public PostEntity findPostById(Long postId) throws PostNotFoundException {
    return repository
        .findById(postId)
        .orElseThrow(() -> new PostNotFoundException("Post with id" + postId + " not found"));
  }
}
