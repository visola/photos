package org.visola.photos.jobs.job;

import org.springframework.stereotype.Component;

@Component
public class PhotoPostProcessor implements Job {

  @Override
  public String getName() {
    return "photos-post-processor";
  }

  @Override
  public void run() {
    System.out.println("photos-post-processor job is running...");
  }

}
