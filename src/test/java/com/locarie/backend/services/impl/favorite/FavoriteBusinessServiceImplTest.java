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
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    List<UserDto> favoredBy =
        userFindUtils.findUserById(users[1].getId()).getFavoredBy().stream()
            .map(userMapper::mapTo)
            .toList();
    List<UserDto> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses().stream()
            .map(userMapper::mapTo)
            .toList();
    thenResultShouldBeExactly(favoredBy, users[0]);
    thenResultShouldBeExactly(favoriteBusinesses, users[1]);
  }

  @Test
  void testRepeatFavoriteBusinessShouldSucceed() {
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    underTests.favoriteBusiness(users[0].getId(), users[1].getId());

    List<UserDto> favoredBy =
        userFindUtils.findUserById(users[1].getId()).getFavoredBy().stream()
            .map(userMapper::mapTo)
            .toList();
    List<UserDto> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses().stream()
            .map(userMapper::mapTo)
            .toList();
    thenResultShouldBeExactly(favoredBy, users[0]);
    thenResultShouldBeExactly(favoriteBusinesses, users[1]);
  }

  @Test
  void testUnfavoriteAfterFavoriteShouldSucceed() {
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    underTests.unfavoriteBusiness(users[0].getId(), users[1].getId());
    List<UserEntity> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses();
    List<UserEntity> favoredBy = userFindUtils.findUserById(users[1].getId()).getFavoredBy();
    assertThat(favoriteBusinesses.isEmpty()).isTrue();
    assertThat(favoredBy.isEmpty()).isTrue();
  }

  @Test
  void testRepeatUnfavoriteAfterFavoriteShouldSucceed() {
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    underTests.unfavoriteBusiness(users[0].getId(), users[1].getId());
    underTests.unfavoriteBusiness(users[0].getId(), users[1].getId());
    List<UserEntity> favoriteBusinesses =
        userFindUtils.findUserById(users[0].getId()).getFavoriteBusinesses();
    List<UserEntity> favoredBy = userFindUtils.findUserById(users[1].getId()).getFavoredBy();
    assertThat(favoriteBusinesses.isEmpty()).isTrue();
    assertThat(favoredBy.isEmpty()).isTrue();
  }

  @Test
  void testListFavoriteAfterFavoriteShouldReturnCorrectData() {
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    List<UserDto> favoriteBusinesses = underTests.listFavoriteBusinesses(users[0].getId());
    thenResultShouldBeExactly(favoriteBusinesses, users[1]);
  }

  @Test
  void testListFavoredByAfterFavoriteShouldReturnCorrectData() {
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    List<UserDto> favoredBy = underTests.listFavoredBy(users[1].getId());
    thenResultShouldBeExactly(favoredBy, users[0]);
  }

  @Test
  void testCountFavoriteAfterFavoriteShouldReturnCorrectResult() {
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    int count = underTests.countFavoriteBusinesses(users[0].getId());
    assertThat(count).isEqualTo(1);
  }

  @Test
  void testCountFavoredByAfterFavoriteShouldReturnCorrectResult() {
    UserDto[] users = favoriteBusinessAfterCreatingUsers();
    int count = underTests.countFavoredBy(users[1].getId());
    assertThat(count).isEqualTo(1);
  }

  private UserDto[] favoriteBusinessAfterCreatingUsers() {
    UserDto user = userTestsDataCreator.givenPlainUserAfterCreated();
    UserDto businessUser = userTestsDataCreator.givenBusinessUserJoleneHornseyAfterCreated();
    underTests.favoriteBusiness(user.getId(), businessUser.getId());
    user.setFavoriteBusinessesCount(1);
    businessUser.setFavoredByCount(1);
    return new UserDto[] {user, businessUser};
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
