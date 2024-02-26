package com.locarie.backend.services.redis.impl;

import com.locarie.backend.services.redis.RedisService;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
  private final RedisTemplate<String, Object> redis;

  public RedisServiceImpl(RedisTemplate<String, Object> redis) {
    this.redis = redis;
  }

  @Override
  public boolean hasKey(String key) {
    return Boolean.TRUE.equals(redis.hasKey(key));
  }

  @Override
  public void set(String key, String value) {
    redis.opsForValue().set(key, value);
  }

  @Override
  public Object get(String key) {
    return key == null ? null : redis.opsForValue().get(key);
  }

  @Override
  public void delete(String key) {
    redis.delete(key);
  }

  @Override
  public void setExpireInMinutes(String key, long expire) {
    redis.expire(key, expire, TimeUnit.MINUTES);
  }
}
