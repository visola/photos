package org.visola.lifebooster.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.visola.lifebooster.model.Project;

public interface ProjectDao {

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO project (name, created, user_id)"
      + " VALUES (:name, :created, :userId)")
  long create(@BindBean Project project);

  @RegisterBeanMapper(Project.class)
  @SqlQuery("SELECT id, name, created, user_id"
      + " FROM project"
      + " WHERE user_id = :userId")
  List<Project> list(@Bind("userId") long userId);

}
