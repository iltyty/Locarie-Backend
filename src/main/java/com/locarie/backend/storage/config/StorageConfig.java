package com.locarie.backend.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("storage")
public class StorageConfig {
  private String type = "local";
  private String urlPrefix = "http://localhost:8080/";
}
