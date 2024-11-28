package com.locarie.backend.services.impl.post.read;

import static org.assertj.core.api.Assertions.assertThat;

import com.locarie.backend.datacreators.LocationCreator;
import com.locarie.backend.datacreators.post.PostTestsDataCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.services.post.impl.PostReadServiceImpl;
import com.locarie.backend.utils.LocationBoundUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    thenResultShouldBeEmpty(listResult);
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
  void testListNearbyAllShouldReturnFirstPostOfEachUser() {
    List<PostDto> postDtosOfJoleneHornsey =
        postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    List<PostDto> postDtosOfShreeji = postTestsDataCreator.givenPostDtosShreejiAfterCreated();
    Point location = postDtosOfJoleneHornsey.getFirst().getUser().getLocation();
    Page<PostDto> listResult = whenListNearbyAllPosts(location, 0, 5);
    List<PostDto> expectedPostDtos =
        List.of(postDtosOfJoleneHornsey.getLast(), postDtosOfShreeji.getLast());
    thenResultShouldContainAllPosts(listResult.getContent(), expectedPostDtos);

    listResult = whenListNearbyAllPosts(location, 1, 1);
    expectedPostDtos = List.of(postDtosOfShreeji.getLast());
    thenResultShouldContainAllPosts(listResult.getContent(), expectedPostDtos);
  }

  @Test
  void testListNearbyAllShouldReturnPostsAccordingToTimeAndDistance() {
    List<PostDto> postDtosOfJoleneHornsey =
        postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    List<PostDto> postDtosOfShreeji = postTestsDataCreator.givenPostDtosShreejiAfterCreated();
    Point location = postDtosOfShreeji.getFirst().getUser().getLocation();
    Page<PostDto> listResult = whenListNearbyAllPosts(location, 0, 5);
    List<PostDto> expectedPostDtos =
        List.of(postDtosOfShreeji.getLast(), postDtosOfJoleneHornsey.getLast());
    thenResultShouldContainAllPosts(listResult.getContent(), expectedPostDtos);

    listResult = whenListNearbyAllPosts(location, 1, 1);
    expectedPostDtos = List.of(postDtosOfJoleneHornsey.getLast());
    thenResultShouldContainAllPosts(listResult.getContent(), expectedPostDtos);
  }

  @Test
  void testListPostsWithinBoundShouldReturnFirstPostOfEachUserInBounds() {
    List<PostDto> posts1 = postTestsDataCreator.givenPostDtosShreejiAfterCreated();
    List<PostDto> posts2 = postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    Point[] bound = LocationBoundUtil.postLocationBound(posts1.getLast(), posts2.getLast());
    List<PostDto> result = whenListWithin(bound);
    List<PostDto> expect = List.of(posts1.getLast(), posts2.getLast());
    thenResultShouldContainAllPosts(result, expect);
  }

  @Test
  void testListPostWithinZeroBoundShouldReturnEmptyResult() {
    postTestsDataCreator.givenPostDtosShreejiAfterCreated();
    postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    Point[] bound = emptyBound();
    List<PostDto> result = whenListWithin(bound);
    thenResultShouldBeEmpty(result);
  }

  private Point[] emptyBound() {
    return new Point[] {LocationCreator.location(0, 0), LocationCreator.location(0, 0)};
  }

  @Test
  void testListUserPostsShouldReturnAllPostsOfUser() {
    List<PostDto> dtos = postTestsDataCreator.givenPostDtosJoleneHornseyAfterCreated();
    Page<PostDto> listResult =
        underTests.listUserPosts(dtos.getFirst().getUser().getId(), PageRequest.of(0, 10));
    thenResultShouldContainAllPosts(listResult.getContent(), dtos);
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

  private Page<PostDto> whenListNearbyAllPosts(Point location, int page, int pageSize) {
    return underTests.listNearbyAll(
        location.getY(), location.getX(), PageRequest.of(page, pageSize));
  }

  private List<PostDto> whenListWithin(Point[] bound) {
    return underTests.listWithin(
        bound[1].getY(), bound[0].getY(), bound[1].getX(), bound[0].getX());
  }

  private void thenResultShouldContainAllPosts(List<PostDto> result, List<PostDto> postDtos) {
    assertThat(result.size()).isEqualTo(postDtos.size());
    for (PostDto postDto : postDtos) {
      assertThat(result).contains(postDto);
    }
  }

  private void thenResultShouldBeEmpty(List<PostDto> result) {
    assertThat(result.isEmpty()).isTrue();
  }
}
