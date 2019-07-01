package org.visola.photos.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.visola.photos.config.GoogleCloudConfiguration;
import org.visola.photos.config.JDBIConfiguration;

@Import({GoogleCloudConfiguration.class, JDBIConfiguration.class,})
@SpringBootApplication
public class JobsMain {

  public static void main (String [] args) {
    SpringApplication.run(JobsMain.class, args);
  }

}
