package com.locarie.backend.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@AllArgsConstructor
@Builder
@RedisHash("reset_password_entry")
@AccessType(AccessType.Type.PROPERTY)
public class ResetPasswordEntry {
  @Id private String email;
  private String code;
  private boolean validated;
  @TimeToLive private Long ttl;
}
