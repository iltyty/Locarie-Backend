package com.locarie.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@Entity
public class User {
    @Id
    private Long id;
    private Type type;
    private String username;
    private String avatarUrl;
    // The following fields are only valid for business User
    private String coverUrl;
    private String homepageUrl;  // business homepage
    private String category;  // business category. e.g., cafe/restaurant, hotel, etc.
    private String introduction;  // business introduction
    private String phone;
    private Point location;
    private String locationName;

    enum Type {
        PLAIN, BUSINESS
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeoCoordinate {
        private Double latitude;
        private Double longitude;
    }
}
