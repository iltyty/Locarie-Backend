package com.locarie.backend.datacreators.post;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.impl.PostEntityDtoMapper;
import com.locarie.backend.repositories.PostRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostTestsDataCreator {
  @Autowired private PostRepository postRepository;
  @Autowired private PostEntityDtoMapper mapper;
  @Autowired private UserTestsDataCreator userTestsDataCreator;

  public PostDto givenPostDtoJoleneHornsey1AfterCreated() {
    long userId = userTestsDataCreator.givenBusinessUserJoleneHornseyIdAfterCreated();
    PostEntity postEntity = PostEntityCreator.postEntityJoleneHornsey1();
    postEntity.setId(null);
    postEntity.getUser().setId(userId);
    PostEntity savedPostEntity = postRepository.save(postEntity);
    return mapper.mapTo(savedPostEntity);
  }

  public PostDto givenPostDtoJoleneHornsey2AfterCreated() {
    long userId = userTestsDataCreator.givenBusinessUserJoleneHornseyIdAfterCreated();
    PostEntity postEntity = PostEntityCreator.postEntityJoleneHornsey2();
    postEntity.setId(null);
    postEntity.getUser().setId(userId);
    PostEntity savedPostEntity = postRepository.save(postEntity);
    return mapper.mapTo(savedPostEntity);
  }

  public List<PostDto> givenPostDtosJoleneHornseyAfterCreated() {
    return List.of(
        givenPostDtoJoleneHornsey1AfterCreated(), givenPostDtoJoleneHornsey2AfterCreated());
  }

  public PostDto givenPostDtoShreeji1AfterCreated() {
    long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    PostEntity postEntity = PostEntityCreator.postEntityShreeji1();
    postEntity.setId(null);
    postEntity.getUser().setId(userId);
    PostEntity savedPostEntity = postRepository.save(postEntity);
    return mapper.mapTo(savedPostEntity);
  }

  public PostDto givenPostDtoShreeji2AfterCreated() {
    long userId = userTestsDataCreator.givenBusinessUserShreejiIdAfterCreated();
    PostEntity postEntity = PostEntityCreator.postEntityShreeji2();
    postEntity.setId(null);
    postEntity.getUser().setId(userId);
    PostEntity savedPostEntity = postRepository.save(postEntity);
    return mapper.mapTo(savedPostEntity);
  }

  public List<PostDto> givenPostDtosShreejiAfterCreated() {
    return List.of(givenPostDtoShreeji1AfterCreated(), givenPostDtoShreeji2AfterCreated());
  }
}
