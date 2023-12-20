package com.locarie.backend.services.impl.post;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.services.post.PostCreateService;
import com.locarie.backend.services.post.PostReadService;
import com.locarie.backend.services.post.PostService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostServiceImpl implements PostService {
  private final PostCreateService createService;
  private final PostReadService readService;

  public PostServiceImpl(
      @Qualifier("PostCreate") PostCreateService createService,
      @Qualifier("PostRead") PostReadService readService) {
    this.createService = createService;
    this.readService = readService;
  }

  @Override
  public PostDto create(PostDto dto, MultipartFile[] images) {
    return createService.create(dto, images);
  }

  @Override
  public Optional<PostDto> get(Long id) {
    return readService.get(id);
  }

  @Override
  public List<PostDto> list() {
    return readService.list();
  }

  @Override
  public List<PostDto> listNearby(double latitude, double longitude, double distance) {
    return readService.listNearby(latitude, longitude, distance);
  }
}
