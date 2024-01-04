package com.locarie.backend.datacreators.post;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.post.PostEntityDtoMapper;

public class PostDtoCreator {
  private static final Mapper<PostEntity, PostDto> mapper = new PostEntityDtoMapper();

  public static PostDto postDtoJoleneHornsey1() {
    return mapper.mapTo(PostEntityCreator.postEntityJoleneHornsey1());
  }

  public static PostDto postDtoJoleneHornsey2() {
    return mapper.mapTo(PostEntityCreator.postEntityJoleneHornsey2());
  }

  public static PostDto postDtoShreeji1() {
    return mapper.mapTo(PostEntityCreator.postEntityShreeji1());
  }

  public static PostDto postDtoShreeji2() {
    return mapper.mapTo(PostEntityCreator.postEntityShreeji2());
  }
}
