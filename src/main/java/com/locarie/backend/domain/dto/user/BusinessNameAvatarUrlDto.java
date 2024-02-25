package com.locarie.backend.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessNameAvatarUrlDto {
  private Long id;
  private String avatarUrl;
  private String businessName;
}
