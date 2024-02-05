package com.locarie.backend.services.impl.post.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.services.impl.post.PostReadServiceImpl;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class PostReadServiceImplListTest {
  @Autowired private PostReadServiceImpl underTests;
  @Autowired private PostTestsDataCreator postTestsDataCreator;

  @Test
  void testListShouldReturnAllPosts() {
    List<PostDto> postDtos = postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    List<PostDto> listResult = whenListAllPosts();
    thenResultShouldContainAllPosts(listResult, postDtos);
  }

  @Test
  void testListNearbyWithin0kmShouldReturnNoPosts() {
    postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    Point location = givenEmptyLocation();
    List<PostDto> listResult = whenListNearbyPostsWithin0km(location);
    thenResultShouldContainNoPost(listResult);
  }

  @Test
  void testListNearbyWithinInfiniteDistanceShouldReturnFirstPostOfEachUser() {
    List<PostDto> postDtosOfJoleneHornsey =
        postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    List<PostDto> postDtosOfShreeji = postTestsDataCreator.givenPostDtosShreejiAfterCreated();
    Point location = postDtosOfJoleneHornsey.getFirst().getUser().getLocation();
    List<PostDto> listResult = whenListNearbyPostsWithinInfiniteDistance(location);
    List<PostDto> expectedPostDtos =
        List.of(postDtosOfJoleneHornsey.getLast(), postDtosOfShreeji.getLast());
    thenResultShouldContainAllPosts(listResult, expectedPostDtos);
  }

  @Test
  void testListUserPostsShouldReturnAllPostsOfUser() {
    List<PostDto> dtos = postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    List<PostDto> listResult = underTests.listUserPosts(dtos.getFirst().getUser().getId());
    thenResultShouldContainAllPosts(listResult, dtos);
  }

  private Point givenEmptyLocation() {
    return new GeometryFactory().createPoint(new Coordinate(0, 0));
  }

  private List<PostDto> whenListAllPosts() {
    return underTests.list();
  }

  private List<PostDto> whenListNearbyPostsWithin0km(Point location) {
    return underTests.listNearby(location.getY(), location.getX(), 0);
  }

  private List<PostDto> whenListNearbyPostsWithinInfiniteDistance(Point location) {
    return underTests.listNearby(location.getY(), location.getX(), Integer.MAX_VALUE);
  }

  private void thenResultShouldContainAllPosts(List<PostDto> result, List<PostDto> postDtos) {
    assertThat(result.size()).isEqualTo(postDtos.size());
    for (PostDto postDto : postDtos) {
      assertThat(result).contains(postDto);
    }
  }

  private void thenResultShouldContainNoPost(List<PostDto> result) {
    assertThat(result.size()).isEqualTo(0);
  }
}
