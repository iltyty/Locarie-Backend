package com.locarie.backend.services.impl.favorite;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.services.favorite.impl.FavoritePostServiceImpl;
import com.locarie.backend.services.utils.PostFindUtils;
import com.locarie.backend.services.utils.UserFindUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;

@SpringBootTest
@Transactional
public class FavoritePostServiceImplTest {
  @Autowired private FavoritePostServiceImpl underTests;
  @Autowired private UserTestsDataCreator userTestsDataCreator;
  @Autowired private PostTestsDataCreator postTestsDataCreator;
  @Autowired private UserFindUtils userFindUtils;
  @Autowired private PostFindUtils postFindUtils;
  @Autowired private Mapper<UserEntity, UserDto> userMapper;
  @Autowired private Mapper<PostEntity, PostDto> postMapper;

  @Test
  void testFavoritePostShouldSucceed() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    List<UserDto> favoredBy =
        postFindUtils.findPostById(pair.getSecond().getId()).getFavoredBy().stream()
            .map(userMapper::mapTo)
            .toList();
    List<PostDto> favoritePosts =
        userFindUtils.findUserById(pair.getFirst().getId()).getFavoritePosts().stream()
            .map(postMapper::mapTo)
            .toList();
    thenPostFavoredByShouldBeExactly(favoredBy, pair.getFirst());
    thenUserFavoritePostsShouldBeExactly(favoritePosts, pair.getSecond());
  }

  @Test
  void testRepeatFavoritePostShouldSucceed() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    underTests.favoritePost(pair.getFirst().getId(), pair.getSecond().getId());

    List<UserDto> favoredBy =
        postFindUtils.findPostById(pair.getSecond().getId()).getFavoredBy().stream()
            .map(userMapper::mapTo)
            .toList();
    List<PostDto> favoritePosts =
        userFindUtils.findUserById(pair.getFirst().getId()).getFavoritePosts().stream()
            .map(postMapper::mapTo)
            .toList();
    thenPostFavoredByShouldBeExactly(favoredBy, pair.getFirst());
    thenUserFavoritePostsShouldBeExactly(favoritePosts, pair.getSecond());
  }

  @Test
  void testUnfavoriteAfterFavoriteShouldSucceed() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    underTests.unfavoritePost(pair.getFirst().getId(), pair.getSecond().getId());

    List<UserEntity> favoredBy =
        postFindUtils.findPostById(pair.getSecond().getId()).getFavoredBy();
    List<PostEntity> favoritePosts =
        userFindUtils.findUserById(pair.getFirst().getId()).getFavoritePosts();
    thenPostFavoredByShouldBeEmpty(favoredBy);
    thenUserFavoritePostsShouldBeEmpty(favoritePosts);
  }

  @Test
  void testRepeatUnfavoriteAfterFavoriteShouldSucceed() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    underTests.unfavoritePost(pair.getFirst().getId(), pair.getSecond().getId());
    underTests.unfavoritePost(pair.getFirst().getId(), pair.getSecond().getId());

    List<UserEntity> favoredBy =
        postFindUtils.findPostById(pair.getSecond().getId()).getFavoredBy();
    List<PostEntity> favoritePosts =
        userFindUtils.findUserById(pair.getFirst().getId()).getFavoritePosts();
    thenPostFavoredByShouldBeEmpty(favoredBy);
    thenUserFavoritePostsShouldBeEmpty(favoritePosts);
  }

  @Test
  void testListFavoriteAfterFavoriteShouldReturnCorrectResult() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    Page<PostDto> favoritePosts =
        underTests.listFavoritePosts(pair.getFirst().getId(), PageRequest.of(0, 10));
    thenUserFavoritePostsShouldBeExactly(favoritePosts.getContent(), pair.getSecond());
  }

  @Test
  void testListFavoredByAfterFavoriteShouldReturnCorrectData() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    Page<UserDto> favoredBy =
        underTests.listFavoredBy(pair.getSecond().getId(), PageRequest.of(0, 10));
    thenPostFavoredByShouldBeExactly(favoredBy.getContent(), pair.getFirst());
  }

  @Test
  void testCountFavoriteAfterFavoriteShouldReturnCorrectResult() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    int count = underTests.countFavoritePosts(pair.getFirst().getId());
    assertThat(count).isEqualTo(1);
  }

  @Test
  void testCountFavoredByAfterFavoriteShouldReturnCorrectResult() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    int count = underTests.countFavoredBy(pair.getSecond().getId());
    assertThat(count).isEqualTo(1);
  }

  @Test
  void testHasBeenSavedAfterFavoriteShouldReturnTrue() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    boolean hasBeenFollowed =
        underTests.isFavoredBy(pair.getFirst().getId(), pair.getSecond().getId());
    assertThat(hasBeenFollowed).isTrue();
  }

  @Test
  void testHasBeenSavedAfterUnFavoriteShouldReturnTrue() {
    Pair<UserDto, PostDto> pair = favoritePostAfterCreatingUserAndPost();
    underTests.unfavoritePost(pair.getFirst().getId(), pair.getSecond().getId());
    boolean hasBeenFollowed =
        underTests.isFavoredBy(pair.getFirst().getId(), pair.getSecond().getId());
    assertThat(hasBeenFollowed).isFalse();
  }

  private Pair<UserDto, PostDto> favoritePostAfterCreatingUserAndPost() {
    UserDto user = userTestsDataCreator.givenBusinessUserShreejiAfterCreated();
    PostDto post = postTestsDataCreator.givenPostDtoShreeji1AfterCreated();
    underTests.favoritePost(user.getId(), post.getId());
    user.setFavoritePostsCount(1);
    post.setFavoredByCount(1);
    return Pair.of(user, post);
  }

  private void thenPostFavoredByShouldBeExactly(List<UserDto> actual, UserDto expected) {
    listBeExactly(actual, expected);
  }

  private void thenUserFavoritePostsShouldBeExactly(List<PostDto> actual, PostDto expected) {
    listBeExactly(actual, expected);
  }

  private void thenPostFavoredByShouldBeEmpty(List<UserEntity> actual) {
    listBeEmpty(actual);
  }

  private void thenUserFavoritePostsShouldBeEmpty(List<PostEntity> actual) {
    listBeEmpty(actual);
  }

  private <T> void listBeExactly(List<T> actual, T expected) {
    assertThat(actual).isNotNull();
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.getFirst()).isEqualTo(expected);
  }

  private <T> void listBeEmpty(List<T> actual) {
    assertThat(actual).isNotNull();
    assertThat(actual.isEmpty()).isTrue();
  }
}
