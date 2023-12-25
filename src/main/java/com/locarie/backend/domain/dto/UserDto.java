package com.locarie.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.serialization.JtsPointDeserializer;
import com.locarie.backend.serialization.JtsPointSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

  private Long id;

  @NotNull(message = "type = PLAIN/BUSINESS is mandatory") private Type type;

  @NotNull(message = "username is mandatory") @Size(min = 2, max = 20, message = "username must be between 2 and 20 characters")
  private String username;

  @NotNull(message = "first name is mandatory") @Size(min = 2, max = 20, message = "first name must be between 2 and 20 characters")
  private String firstName;

  @NotNull(message = "last name is mandatory") @Size(min = 2, max = 20, message = "last name must be between 2 and 20 characters")
  private String lastName;

  @NotNull(message = "email is mandatory") private String email;

  private String avatarUrl;

  // The following fields are only valid for business users
  private String businessName;
  private List<String> coverUrls;
  private String homepageUrl;
  private String category;
  private String introduction;
  private String phone;
  private Integer openHour;
  private Integer openMinute;
  private Integer closeHour;
  private Integer closeMinute;

  @JsonSerialize(using = JtsPointSerializer.class)
  @JsonDeserialize(using = JtsPointDeserializer.class)
  private Point location;

  private String address;

  public enum Type {
    PLAIN,
    BUSINESS
  }
}
