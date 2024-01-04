package com.locarie.backend.domain.dto.user;

import java.time.Instant;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private Instant birthday;
  private String businessName;
  private String homepageUrl;
  private String category;
  private String introduction;
  private String phone;
}
