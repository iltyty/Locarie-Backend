package com.locarie.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.serialization.JtsPointDeserializer;
import com.locarie.backend.serialization.JtsPointSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private Type type;
    private String username;
    private String email;
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

    @JsonSerialize(using = JtsPointSerializer.class)
    @JsonDeserialize(using = JtsPointDeserializer.class)
    private Point location;       // business location

    private String locationName;  // business location description

    public enum Type {
        PLAIN, BUSINESS
    }
}
