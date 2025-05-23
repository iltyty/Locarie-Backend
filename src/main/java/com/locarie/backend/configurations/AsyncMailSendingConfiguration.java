package com.locarie.backend.configurations;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncMailSendingConfiguration {
  @Bean(name = "emailSendingTaskExecutor")
  public Executor emailSendingTaskExecutor() {
    return new ThreadPoolTaskExecutor();
  }
}
