package com.locarie.backend.repositories;

import com.locarie.backend.domain.entities.PostEntity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
          "select p.*, u.location, u.location_name from posts p join users u on p.user_id"
              + " = u.id where ST_Distance_Sphere(u.location, Point(:longitude,"
              + " :latitude)) <= :distance",
      nativeQuery = true)
  List<PostEntity> findNearby(double latitude, double longitude, int distance);
}
