package com.locarie.backend.services.impl.favorite;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locarie.backend.datacreators.user.UserTestsDataCreator;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.services.favorite.impl.FavoriteBusinessServiceImpl;
import com.locarie.backend.services.utils.UserFindUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class FavoriteBusinessServiceImplTest {
  @Autowired private FavoriteBusinessServiceImpl underTests;
  @Autowired private UserTestsDataCreator userTestsDataCreator;
  @Autowired private UserFindUtils userFindUtils;
  @Autowired private Mapper<UserEntity, UserDto> userMapper;

  @Test
  void testFavoriteBusinessShouldSucceed() {
    UserEntity[] users = favoriteBusinessAfterCreatingUsers();
    List<UserEntity> favoredBy = userFindUtils.findUserById(users[1].getId()).getFavoredBy();
    List<UserEntity> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses();
    thenResultShouldBeExactly(favoredBy, users[0]);
    thenResultShouldBeExactly(favoriteBusinesses, users[1]);
  }

  @Test
  void testRepeatFavoriteBusinessShouldSucceed() {
    UserEntity[] users = favoriteBusinessAfterCreatingUsers();
    underTests.favoriteBusiness(users[0].getId(), users[1].getId());
    List<UserEntity> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses();
    thenResultShouldBeExactly(favoriteBusinesses, users[1]);
  }

  @Test
  void testUnfavoriteAfterFavoriteShouldSucceed() {
    UserEntity[] users = favoriteBusinessAfterCreatingUsers();
    underTests.unfavoriteBusiness(users[0].getId(), users[1].getId());
    List<UserEntity> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses();
    List<UserEntity> favoredBy = userFindUtils.findUserById(users[1].getId()).getFavoredBy();
    assertThat(favoriteBusinesses.isEmpty()).isTrue();
    assertThat(favoredBy.isEmpty()).isTrue();
  }

  @Test
  void testRepeatUnfavoriteAfterFavoriteShouldSucceed() {
    UserEntity[] users = favoriteBusinessAfterCreatingUsers();
    underTests.unfavoriteBusiness(users[0].getId(), users[1].getId());
    underTests.unfavoriteBusiness(users[0].getId(), users[1].getId());
    List<UserEntity> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses();
    List<UserEntity> favoredBy = userFindUtils.findUserById(users[1].getId()).getFavoredBy();
    assertThat(favoriteBusinesses.isEmpty()).isTrue();
    assertThat(favoredBy.isEmpty()).isTrue();
  }

  @Test
  void testListAfterFavoriteShouldReturnCorrectData() {
    UserEntity[] users = favoriteBusinessAfterCreatingUsers();
    List<UserDto> favoriteBusinesses = underTests.listFavoriteBusinesses(users[0].getId());
    UserDto businessUser = userMapper.mapTo(users[1]);
    thenResultShouldBeExactly(favoriteBusinesses, businessUser);
  }

  private UserEntity[] favoriteBusinessAfterCreatingUsers() {
    UserEntity user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserEntity businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    underTests.favoriteBusiness(user.getId(), businessUser.getId());
    return new UserEntity[] {user, businessUser};
  }

  private void thenResultShouldBeExactly(
      List<UserEntity> favoriteBusinesses, UserEntity businessUser) {
    listBeExactly(favoriteBusinesses, businessUser);
  }

  private void thenResultShouldBeExactly(List<UserDto> favoriteBusinesses, UserDto businessUser) {
    listBeExactly(favoriteBusinesses, businessUser);
  }

  private <T> void listBeExactly(List<T> actual, T expected) {
    assertThat(actual).isNotNull();
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.getFirst()).isEqualTo(expected);
  }
}
