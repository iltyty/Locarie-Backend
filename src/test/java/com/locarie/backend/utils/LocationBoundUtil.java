package com.locarie.backend.utils;

import com.locarie.backend.datacreators.LocationCreator;
import com.locarie.backend.domain.dto.post.PostDto;
import java.util.Arrays;
import java.util.List;
import org.locationtech.jts.geom.Point;

public class LocationBoundUtil {
  public static Point[] postLocationBound(PostDto... posts) {
    List<Double> latitudes =
        Arrays.stream(posts).map(x -> x.getUser().getLocation().getY()).toList();
    List<Double> longitudes =
        Arrays.stream(posts).map(x -> x.getUser().getLocation().getX()).toList();
    double minLatitude = latitudes.stream().min(Double::compareTo).orElse(0.0) - 0.001;
    double maxLatitude = latitudes.stream().max(Double::compareTo).orElse(0.0) + 0.001;
    double minLongitude = longitudes.stream().min(Double::compareTo).orElse(0.0) - 0.001;
    double maxLongitude = longitudes.stream().max(Double::compareTo).orElse(0.0) + 0.001;
    return new Point[] {
      LocationCreator.location(minLongitude, maxLatitude),
      LocationCreator.location(maxLongitude, minLatitude)
    };
  }
}
