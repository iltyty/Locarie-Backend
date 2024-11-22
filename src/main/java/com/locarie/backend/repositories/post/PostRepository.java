package com.locarie.backend.repositories.post;

import com.locarie.backend.domain.entities.PostEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long> {
  /**
   * Retrieves all posts near a specified geographical location.
   *
   * <p>This method uses the provided latitude and longitude along with a distance parameter to find
   * posts nearby. The distance is measured in kilometers. It returns a list of PostEntity objects
   * that are located within a circular centered at the specified location with the given distance
   * as its radius.
   *
   * @param latitude The latitude of the central point for the query.
   * @param longitude The longitude of the central point for the query.
   * @param distance The radius of the search area in meters.
   * @return A list of PostEntity objects near the specified location. Returns an empty list if no
   *     posts are found within the given distance.
   */
  @Query(
      value =
          "select p.*, u.location, u.address from posts p join users u on p.user_id = u.id where"
              + " ST_Distance_Sphere(u.location, Point(:longitude, :latitude)) / 1000 <= :distance"
              + " and p.id = (select id from posts where user_id = u.id order by time desc, id desc"
              + " limit 1)",
      nativeQuery = true)
  List<PostEntity> findNearby(@Param(value = "latitude") double latitude, @Param(value = "longitude") double longitude, @Param(value = "distance") double distance);

  @Query(
      value =
          "select p.*, u.location, ST_Distance_Sphere(u.location, Point(:longitude, :latitude)) as"
              + " dist from posts p join users u on p.user_id = u.id where p.id = (select id from"
              + " posts where user_id = u.id order by time desc, id desc limit 1) order by p.time"
              + " desc, dist",
      countQuery = "select count(p.id) from posts p join users u on p.user_id = u.id"
              + " where p.id = (select id from posts where user_id = u.id order by time desc, id desc limit 1)",
      nativeQuery = true)
  Page<PostEntity> findNearbyAll(@Param(value = "latitude") double latitude, @Param(value = "longitude") double longitude, Pageable pageable);

  @Query(value = "select p from PostEntity p where p.user.id = :id")
  List<PostEntity> findByUserId(@Param("id") Long id);

  @Query(
      value =
          "select p1 from PostEntity p1 where p1.id in (select max(p2.id) from PostEntity p2 where"
              + " p2.user.id in :ids group by p2.user.id)")
  List<PostEntity> findByUserIds(@Param(value = "ids") List<Long> ids);

  @Query(
      value =
          "select count(p) from PostEntity p join p.favoredBy u where p.id = :postId and u.id ="
              + " :userId")
  int hasBeenSaved(@Param(value = "userId") Long userId, @Param(value = "postId") Long postId);

  @Query(
      value =
          "select p from PostEntity p where p.id = (select p1.id from PostEntity p1 where p1.user ="
              + " p.user order by p1.time desc, p1.id desc limit 1) and within(p.user.location,"
              + " :bound) = true")
  List<PostEntity> findWithin(@Param(value = "bound") Geometry bound);

  @Query(value="select p.time from PostEntity p where p.user.id = :userId order by p.time desc limit 1")
  Optional<Instant> findLatestPostTimeByUserId(@Param(value="userId") Long userId);
}
