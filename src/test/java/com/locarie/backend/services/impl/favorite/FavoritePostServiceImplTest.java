package com.locarie.backend.services.impl.favorite;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.services.favorite.impl.FavoritePostServiceImpl;
import com.locarie.backend.services.utils.PostFindUtils;
import com.locarie.backend.services.utils.UserFindUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class FavoritePostServiceImplTest {
  @Autowired private FavoritePostServiceImpl underTests;
  @Autowired private UserTestsDataCreator userTestsDataCreator;
  @Autowired private PostTestsDataCreator postTestsDataCreator;
  @Autowired private UserFindUtils userFindUtils;
  @Autowired private PostFindUtils postFindUtils;

  @Test
  void testFavoritePostShouldSucceed() {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    underTests.favoritePost(userId, post.getId());
    List<PostEntity> actual = userFindUtils.findUserById(userId).getFavoritePosts();
    PostEntity expected = postFindUtils.findPostById(post.getId());
    thenUserFavoritePostsShouldBeExactly(actual, expected);
  }

  @Test
  void testUnfavoriteAfterFavoriteShouldSucceed() {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    underTests.favoritePost(userId, post.getId());
    underTests.unfavoritePost(userId, post.getId());
    List<PostEntity> actual = userFindUtils.findUserById(userId).getFavoritePosts();
    assertThat(actual.size()).isEqualTo(0);
  }

  @Test
  void testListAfterFavoriteShouldReturnCorrectData() {
    Long userId = userTestsDataCreator.givenBusinessUserShreejiAfterCreated().getId();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    underTests.favoritePost(userId, post.getId());
    thenUserFavoritePostsShouldBeExactly(underTests.listFavoritePosts(userId), post);
  }

  private void thenUserFavoritePostsShouldBeExactly(List<PostEntity> actual, PostEntity expected) {
    listBeExactly(actual, expected);
  }

  private void thenUserFavoritePostsShouldBeExactly(List<PostDto> actual, PostDto expected) {
    listBeExactly(actual, expected);
  }

  private <T> void listBeExactly(List<T> actual, T expected) {
    assertThat(actual).isNotNull();
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.getFirst()).isEqualTo(expected);
  }
}
