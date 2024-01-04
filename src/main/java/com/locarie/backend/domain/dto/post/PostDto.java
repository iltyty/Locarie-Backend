package com.locarie.backend.domain.dto.post;

import com.locarie.backend.domain.dto.user.UserDto;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;
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

  private Instant time;

  @NotEmpty(message = "title cannot be empty")
  private String title;

  @NotEmpty(message = "content cannot be empty")
  private String content;

  List<String> imageUrls;
}
