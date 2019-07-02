package org.visola.photos.jobs.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobsConfiguration {

  @Bean
  public ScheduledExecutorService executorService() {
    return Executors.newScheduledThreadPool(1);
  }

}
