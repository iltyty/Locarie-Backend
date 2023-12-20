package com.locarie.backend.services.impl.post.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.PostDto;
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
  void testListReturnsAllPosts() {
    List<PostDto> postDtos = postTestsDataCreator.givenPosts("post1", "post2");
    List<PostDto> listResult = whenListAllPosts();
    thenListResultShouldContainAllPosts(listResult, postDtos);
  }

  @Test
  void testListNearbyWithin0kmReturnsNoPosts() {
    postTestsDataCreator.givenPosts("post1", "post2");
    Point location = new GeometryFactory().createPoint(new Coordinate(0, 0));
    List<PostDto> listResult = whenListNearbyPostsWithin0km(location);
    thenListResultShouldContainNoPost(listResult);
  }

  @Test
  void testListNearbyWithinInfiniteDistanceReturnsAllPosts() {
    List<PostDto> postDtos = postTestsDataCreator.givenPosts("post1", "post2");
    Point location = postDtos.getFirst().getUser().getLocation();
    List<PostDto> listResult = whenListNearbyPostsWithinInfiniteDistance(location);
    thenListResultShouldContainAllPosts(listResult, postDtos);
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

  private void thenListResultShouldContainAllPosts(List<PostDto> result, List<PostDto> postDtos) {
    assertThat(result.size()).isEqualTo(postDtos.size());
    for (PostDto postDto : postDtos) {
      assertThat(result).contains(postDto);
    }
  }

  private void thenListResultShouldContainNoPost(List<PostDto> result) {
    assertThat(result.size()).isEqualTo(0);
  }
}
