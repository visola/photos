package org.visola.lifebooster.model;

/**
 * Habit type associates some data type to an habit.
 */
public enum HabitType {

  /**
   * Entries in the journal will count as execution of the habit.
   */
  ENTRY_IN_JOURNAL,

  /**
   * User will manually check the time when an activity associated with this
   * habit happened. Check {@link ManualPractice manual practice}.
   */
  MANUAL,

  ;

}
