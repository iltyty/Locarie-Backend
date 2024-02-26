package com.locarie.backend.services.redis;

public interface RedisService {
  boolean hasKey(String key);

  void set(String key, String value);

  Object get(String key);

  void setExpireInMinutes(String key, long expire);

  long getExpireInMinutes(String key);
}
