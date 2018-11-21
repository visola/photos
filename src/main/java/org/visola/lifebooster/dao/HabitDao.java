package org.visola.lifebooster.dao;

import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.visola.lifebooster.model.Habit;

public interface HabitDao {

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO habit"
      + " ("
      + "   name, user_id, type, created, updated,"
      + "   `interval`, interval_unit,"
      + "   frequency, frequency_type, frequency_unit"
      + " )"
      + " VALUES ("
      + "   :name, :userId, :type, :created, :updated,"
      + "   :interval, :intervalUnit,"
      + "   :frequency, :frequencyType, :frequencyUnit"
      + " )")
  long create(@BindBean Habit habit);

}
