package org.visola.lifebooster.model;

import lombok.Data;

/**
 * A manual practice is something the user manually marks in the application as done and associate
 * it with a {@link Habit} of type {@link HabitType#MANUAL manual}.
 */
@Data
public class ManualPractice {

  private long id;
  private long happenedAt;
  private long habitId;
  private String description;

}
