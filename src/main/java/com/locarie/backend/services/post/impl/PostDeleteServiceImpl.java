package com.locarie.backend.services.post.impl;

import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.post.PostDeleteService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PostDeleteServiceImpl implements PostDeleteService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostDeleteServiceImpl(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  @Override
  public boolean delete(Long postId) {
    Optional<PostEntity> post = postRepository.findById(postId);
    if (post.isEmpty()) {
      return true;
    }
    postRepository.deleteById(postId);

    UserEntity user = post.get().getUser();
    Optional<Instant> lastUpdate = postRepository.findLatestPostTimeByUserId(user.getId());
    if (lastUpdate.isEmpty()) {
      user.setLastUpdate(null);
    } else {
      user.setLastUpdate(lastUpdate.get());
    }
    userRepository.save(user);
    return true;
  }
}
