package com.locarie.backend.services.post.impl;

import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.services.post.PostDeleteService;
import org.springframework.stereotype.Service;

@Service
public class PostDeleteServiceImpl implements PostDeleteService {
  private final PostRepository repository;

  public PostDeleteServiceImpl(PostRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean delete(Long postId) {
    repository.deleteById(postId);
    return true;
  }
}
