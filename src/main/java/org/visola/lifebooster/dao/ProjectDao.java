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
import org.visola.lifebooster.model.Project;

public interface ProjectDao {

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO project (name, created, user_id)"
      + " VALUES (:name, :created, UUID_TO_BIN(:userId))")
  long create(@BindBean Project project);

  @RegisterBeanMapper(Project.class)
  @SqlQuery("SELECT id, name, created, BIN_TO_UUID(user_id) AS user_id"
      + " FROM project"
      + " WHERE user_id = UUID_TO_BIN(:userId)")
  List<Project> list(@Bind("userId") UUID userId);

}
