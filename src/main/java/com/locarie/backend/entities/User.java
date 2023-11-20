package com.locarie.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private Type type;
    private String username;
    private String avatarUrl;
    // The following fields are only valid for business users
    private String coverUrl;
    private String homepageUrl;   // business homepage
    private String category;      // business category. e.g., cafe/restaurant, hotel, etc.
    private String introduction;  // business introduction
    private String phone;         // business phone number
    private Integer openHour;     // business opening hour
    private Integer openMinute;   // business opening minute
    private Integer closeHour;    // business closing hour
    private Integer closeMinute;  // business closing minute
    private Point location;       // business location
    private String locationName;  // business location description

    public enum Type {
        PLAIN, BUSINESS
    }
}
