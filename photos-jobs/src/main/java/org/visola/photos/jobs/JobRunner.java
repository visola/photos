package org.visola.photos.jobs;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.visola.photos.jobs.job.Job;

@Component
public class JobRunner {

  private final ApplicationArguments arguments;
  private final ScheduledExecutorService executorService;

  public JobRunner(ApplicationArguments arguments, List<Job> jobs, ScheduledExecutorService executorService) {
    this.arguments = arguments;
    this.executorService = executorService;

    for (Job job : jobs) {
      if (arguments.containsOption(job.getName())) {
        executorService.scheduleWithFixedDelay(() -> job.run(), 0L, 1L, TimeUnit.SECONDS);
        return;
      }
    }

    System.out.println("No job found to run. Exiting...");
    System.out.println();
    System.out.println("Usage:");
    System.out.println("   photos-job --{processor-name}");
    System.out.println();
    System.out.println("Jobs available:");
    for (Job job : jobs) {
      System.out.printf("  - %s\n", job.getName());
    }
    System.exit(-1);
  }

}


