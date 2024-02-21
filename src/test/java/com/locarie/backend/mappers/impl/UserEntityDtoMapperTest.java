package com.locarie.backend.mappers.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.post.PostEntityCreator;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserEntityDtoMapperTest {
  @Autowired private Mapper<UserEntity, UserDto> mapper;

  @Test
  void testUserEntityToUserDtoMappingShouldSucceed() {
    UserEntity entity = UserEntityCreator.businessUserEntityShreeji();
    entity.setFavoredBy(List.of(UserEntityCreator.plainUserEntity()));
    entity.setFavoriteBusinesses(List.of(UserEntityCreator.businessUserEntityJoleneHornsey()));
    entity.setFavoritePosts(List.of(PostEntityCreator.postEntityShreeji1()));

    UserDto dto = mapper.mapTo(entity);
    assertThat(dto.getFavoredByCount()).isEqualTo(entity.getFavoredBy().size());
    assertThat(dto.getFavoritePostsCount()).isEqualTo(entity.getFavoritePosts().size());
    assertThat(dto.getFavoriteBusinessesCount()).isEqualTo(entity.getFavoriteBusinesses().size());
  }
}
