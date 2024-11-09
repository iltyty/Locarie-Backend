package com.locarie.backend.services.post.impl;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.impl.post.PostEntityDtoMapper;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.post.PostCreateService;
import com.locarie.backend.services.utils.UserFindUtils;
import com.locarie.backend.storage.StorageService;
import com.locarie.backend.storage.utils.StorageUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("PostCreate")
public class PostCreateServiceImpl implements PostCreateService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final UserFindUtils userFindUtils;

  private final PostEntityDtoMapper mapper;

  private final StorageService storageService;

  public PostCreateServiceImpl(
      PostRepository postRepository,
      UserRepository userRepository,
      UserFindUtils userFindUtils,
      PostEntityDtoMapper mapper,
      StorageService storageService) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.userFindUtils = userFindUtils;
    this.mapper = mapper;
    this.storageService = storageService;
  }

  @Override
  public PostDto create(PostDto postDto, MultipartFile[] images) {
    PostEntity postEntity = createPost(postDto);
    List<String> imageUrls = savePostImages(postEntity, images);
    updatePostEntityImageUrls(postEntity, imageUrls);
    updateUserLastUpdate(postEntity.getUser().getId());
    return mapper.mapTo(postEntity);
  }

  private List<String> savePostImages(PostEntity postEntity, MultipartFile[] images) {
    Long postId = postEntity.getId();
    Long userId = postEntity.getUser().getId();
    String dirname = StorageUtil.getPostImagesDirname(userId, postId);
    // ATTENTION: need to return a modifiable list here
    return Arrays.stream(images)
        .map(image -> storageService.store(image, dirname))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private PostEntity createPost(PostDto postDto) {
    PostEntity postEntity = mapper.mapFrom(postDto);
    return postRepository.save(postEntity);
  }

  private void updatePostEntityImageUrls(PostEntity postEntity, List<String> imageUrls) {
    postEntity.setImageUrls(imageUrls);
    postRepository.save(postEntity);
  }

  private void updateUserLastUpdate(Long userId) {
    UserEntity user = userFindUtils.findUserById(userId);
    user.setLastUpdate(Instant.now());
    userRepository.save(user);
  }
}
