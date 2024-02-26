package com.locarie.backend.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@AllArgsConstructor
@Builder
@RedisHash("reset_password_entry")
public class ResetPasswordEntry {
  @Id private Long id;
  private String code;
  private boolean validated;

  @TimeToLive private Long ttl;
}
