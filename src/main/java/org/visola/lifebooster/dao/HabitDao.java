package org.visola.lifebooster.dao;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
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

  @RegisterBeanMapper(Habit.class)
  @SqlQuery("SELECT * FROM habit WHERE id = :habitId AND user_id = :userId")
  Optional<Habit> findByIdAndUserId(@Bind("habitId") long id, @Bind("userId") long userId);

  @RegisterBeanMapper(Habit.class)
  @SqlQuery("SELECT * FROM habit WHERE user_id = :userId")
  List<Habit> findByUserId(@Bind("userId") long id);

  @SqlUpdate("UPDATE habit SET"
      + " name = :name, user_id = :userId, type = :type, created = :created, updated = :updated,"
      + " `interval` = :interval, interval_unit = :intervalUnit, frequency = :frequency,"
      + " frequency_type = :frequencyType, frequency_unit = :frequencyUnit"
      + " WHERE id = :id")
  void update(@BindBean Habit loaded);

}
