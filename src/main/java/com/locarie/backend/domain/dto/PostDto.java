package com.locarie.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
  private Long id;
  private UserDto user;
  private Date time;

  @NotEmpty(message = "title cannot be empty")
  private String title;

  @NotEmpty(message = "content cannot be empty")
  private String content;
}
