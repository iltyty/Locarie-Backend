package com.locarie.backend.services.impl.user.delete;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.services.favorite.FavoriteBusinessService;
import com.locarie.backend.services.favorite.FavoritePostService;
import com.locarie.backend.services.user.impl.UserDeleteServiceImpl;
import com.locarie.backend.services.utils.PostFindUtils;
import com.locarie.backend.services.utils.UserFindUtils;
import com.locarie.backend.storage.StorageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserDeleteIntegrationTests {
  @Autowired private UserDeleteServiceImpl userDeleteService;
  @Autowired private FavoritePostService favoritePostService;
  @Autowired private FavoriteBusinessService favoriteBusinessService;

  @Autowired private UserFindUtils userFindUtils;
  @Autowired private PostRepository postRepository;
  @Autowired private PostFindUtils postFindUtils;

  @Autowired private StorageService storageService;

  @Autowired private UserTestsDataCreator userTestsDataCreator;
  @Autowired private PostTestsDataCreator postTestsDataCreator;

  @Test
  void testDeletingUserShouldDeleteAllUserPosts() {
    Long userId = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated().getId();
    givenJoleneHornseyPosts();
    userDeleteService.delete(userId);
    thenUserPostsShouldBeEmpty(userId);
  }

  @Test
  void testDeletingUserShouldDecreasePostFavoredByCountByOne() {
    Long userId = userTestsDataCreator.givenPlainUserAfterCreated().getId();
    PostDto[] posts = givenJoleneHornseyPosts();
    favoritePostService.favoritePost(userId, posts[0].getId());
    userDeleteService.delete(userId);
    thenPostFavoredByCountShouldBeZero(posts[0].getId());
  }

  @Test
  void testDeletingUserShouldDecreaseBusinessFavoredByCountByOne() {
    Long userId = userTestsDataCreator.givenPlainUserAfterCreated().getId();
    UserDto business = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    favoriteBusinessService.favoriteBusiness(userId, business.getId());
    userDeleteService.delete(userId);
    thenBusinessFavoredByShouldBeZero(business.getId());
  }

  @Test
  void testDeletingUserShouldDeleteResourceFolders() {
    Long userId = userTestsDataCreator.givenPlainUserAfterCreated().getId();
    userDeleteService.delete(userId);
    thenUserDirShouldNotExist(userId);
  }

  private PostDto[] givenJoleneHornseyPosts() {
    return new PostDto[] {
      postTestsDataCreator.givenPostDtoJoleneHornsey1AfterCreated(),
      postTestsDataCreator.givenPostDtoJoleneHornsey2AfterCreated(),
    };
  }

  private void thenUserPostsShouldBeEmpty(Long userId) {
    assertThat(postRepository.findByUserId(userId).isEmpty()).isTrue();
  }

  private void thenPostFavoredByCountShouldBeZero(Long postId) {
    assertThat(postFindUtils.findPostById(postId).getFavoredBy().size()).isEqualTo(0);
  }

  private void thenBusinessFavoredByShouldBeZero(Long id) {
    assertThat(userFindUtils.findUserById(id).getFavoredBy().size()).isEqualTo(0);
  }

  private void thenUserDirShouldNotExist(Long userId) {
    assertThat(storageService.getUserDataDirPath(userId).toFile().exists()).isFalse();
  }
}
