package org.visola.lifebooster.dao;

import java.util.List;
import java.util.UUID;

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
      + " VALUES (:entry, :date, UUID_TO_BIN(:userId))")
  long create(@BindBean JournalEntry journalEntry);

  @RegisterBeanMapper(JournalEntry.class)
  @SqlQuery("SELECT id, entry, date, BIN_TO_UUID(user_id) AS user_id"
      + " FROM journal_entry"
      + " WHERE user_id = UUID_TO_BIN(:userId)")
  List<JournalEntry> list(@Bind("userId") UUID userId);

}
