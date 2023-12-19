package com.locarie.backend.utils;

import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.utils.post.PostEntityCreator;
import com.locarie.backend.utils.user.UserEntityCreator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostTestsDataCreator {
  @Autowired private PostRepository postRepository;
  @Autowired private PostEntityDtoMapper postEntityDtoMapper;
  @Autowired private UserRepository userRepository;

  private UserEntity user;

  public List<PostDto> givenPosts(String... postNames) {
    registerBusinessUserJoleneHornsey();
    List<PostDto> posts = new ArrayList<>();
    for (String postName : postNames) {
      PostEntity postEntity;
      switch (postName) {
        case "post1":
          postEntity = PostEntityCreator.newPostEntityJoleneHornsey1(user);
          break;
        case "post2":
          postEntity = PostEntityCreator.newPostEntityJoleneHornsey2(user);
          break;
        default:
          continue;
      }
      PostEntity savedPostEntity = postRepository.save(postEntity);
      PostDto savedPostDto = postEntityDtoMapper.mapTo(savedPostEntity);
      posts.add(savedPostDto);
    }
    return posts;
  }

  private void registerBusinessUserJoleneHornsey() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    userEntity.setId(null);
    user = userRepository.save(userEntity);
  }
}
