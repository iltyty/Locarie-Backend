package com.locarie.backend.services.impl.post;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.exceptions.UserNotFoundException;
import com.locarie.backend.mapper.impl.post.PostEntityDtoMapper;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.services.post.PostReadService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import com.locarie.backend.services.utils.UserFindUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service("PostRead")
public class PostReadServiceImpl implements PostReadService {
  private final PostRepository repository;

  private final PostEntityDtoMapper mapper;

  public PostReadServiceImpl(PostRepository repository, PostEntityDtoMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<PostDto> get(Long id) {
    Optional<PostEntity> result = repository.findById(id);
    return result.map(mapper::mapTo);
  }

  @Override
  public List<PostDto> list() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(mapper::mapTo)
        .toList();
  }

  @Override
  public List<PostDto> listNearby(double latitude, double longitude, double distance) {
    return repository.findNearby(latitude, longitude, distance).stream()
        .map(mapper::mapTo)
        .toList();
  }

  @Override
  public List<PostDto> listUserPosts(Long id) {
    return repository.findByUserId(id).stream().map(mapper::mapTo).toList();
  }
}
