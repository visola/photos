package org.visola.lifebooster.model;

import lombok.Data;

/**
 * A habit represents some practice that the user wants to get done with some frequency
 * during some time interval.
 *
 * Example of habit interval and frequency combinations:
 * <ul>
 * <li>3 hours per week: interval unit is week, frequency type is time and unit is hours
 * <li>4 times a month: interval unit is month, frequency type is unit and unit is null
 * </ul>
 */
@Data
public class Habit {

  private long id;
  private long userId;
  private String name;
  private HabitType type;

  private long created;
  private long updated;

  private int interval;
  private IntervalUnit intervalUnit;

  private int frequency;
  private FrequencyType frequencyType;
  private IntervalUnit frequencyUnit;

}
