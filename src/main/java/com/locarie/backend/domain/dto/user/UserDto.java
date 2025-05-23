package com.locarie.backend.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.locarie.backend.domain.dto.businesshours.BusinessHoursDto;
import com.locarie.backend.serialization.deserializers.JtsPointDeserializer;
import com.locarie.backend.serialization.serializers.JtsPointSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(exclude = {"businessHours"})
@EqualsAndHashCode(exclude = {"businessHours"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
  private Long id;

  @NotNull(message = "type = PLAIN/BUSINESS is mandatory") private Type type;

  private String username;

  @NotNull(message = "first name is mandatory") @Size(min = 1, max = 25, message = "first name must be between 1 and 25 characters")
  private String firstName;

  @NotNull(message = "last name is mandatory") @Size(min = 1, max = 25, message = "last name must be between 1 and 25 characters")
  private String lastName;

  @NotNull(message = "email is mandatory") private String email;

  private String avatarUrl;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant birthday;

  private int favoredByCount;
  private int favoritePostsCount;
  private int favoriteBusinessesCount;

  // The following fields are only valid for business users
  private String businessName;
  private List<String> categories;
  private List<String> profileImageUrls;
  private String homepageUrl;
  private String introduction;
  private String phone;

  @JsonManagedReference private List<BusinessHoursDto> businessHours;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
  private Instant lastUpdate;

  @JsonSerialize(using = JtsPointSerializer.class)
  @JsonDeserialize(using = JtsPointDeserializer.class)
  private Point location;

  private String address;
  private String neighborhood;

  public enum Type {
    PLAIN,
    BUSINESS
  }
}
