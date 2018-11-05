package org.visola.lifebooster.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.visola.lifebooster.model.JournalEntry;

public interface JournalEntryDao {

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO journal_entry (entry, date, user_id)"
      + " VALUES (:entry, :date, :userId)")
  long create(@BindBean JournalEntry journalEntry);

  @SqlQuery("SELECT date FROM journal_entry WHERE user_id = :userId")
  List<Long> fetchEntrtDates(@Bind("userId") long userId);

  @RegisterBeanMapper(JournalEntry.class)
  @SqlQuery("SELECT id, entry, date, user_id"
      + " FROM journal_entry"
      + " WHERE user_id = :userId")
  List<JournalEntry> list(@Bind("userId") long userId);

}
