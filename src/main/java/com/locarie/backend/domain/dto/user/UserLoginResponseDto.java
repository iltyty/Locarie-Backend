package com.locarie.backend.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponseDto {
  private Long id;
  private String email;
  private String type;
  private String username;
  private String avatarUrl;
  private String jwtToken;
}
