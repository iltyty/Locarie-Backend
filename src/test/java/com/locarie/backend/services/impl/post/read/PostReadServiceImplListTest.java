package com.locarie.backend.services.impl.post.read;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.PostDto;
import com.locarie.backend.services.impl.post.PostReadServiceImpl;
import jakarta.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class PostReadServiceImplListTest {
  @Autowired private PostReadServiceImpl underTests;
  @Autowired private PostTestsDataCreator postTestsDataCreator;

  @Test
  void testListShouldReturnAllPosts() {
    List<PostDto> postDtos = postTestsDataCreator.givenPosts("post1", "post2");
    List<PostDto> listResult = whenListAllPosts();
    thenResultShouldContainAllPosts(listResult, postDtos);
  }

  @Test
  void testListNearbyWithin0kmShouldReturnNoPosts() {
    postTestsDataCreator.givenPosts("post1", "post2");
    Point location = givenEmptyLocation();
    List<PostDto> listResult = whenListNearbyPostsWithin0km(location);
    thenResultShouldContainNoPost(listResult);
  }

  @Test
  void testListNearbyWithinInfiniteDistanceShouldReturnFirstPostOfEachUser() {
    List<PostDto> postDtos = postTestsDataCreator.givenPosts("post1", "post2");
    Point location = postDtos.getFirst().getUser().getLocation();
    List<PostDto> listResult = whenListNearbyPostsWithinInfiniteDistance(location);
    thenResultShouldContainPost(listResult, postDtos.getLast());
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

  private void thenResultShouldContainPost(List<PostDto> result, PostDto postDto) {
    assertThat(result.size()).isEqualTo(1);
    assertThat(result).contains(postDto);
  }
}
