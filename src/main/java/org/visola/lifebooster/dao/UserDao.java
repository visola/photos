package org.visola.lifebooster.dao;

import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.visola.lifebooster.model.User;

public interface UserDao {

  @SqlUpdate("INSERT INTO user (id, email) VALUES (UUID_TO_BIN(:id), :email)")
  void create(@BindBean User user);

  @RegisterBeanMapper(User.class)
  @SqlQuery("SELECT BIN_TO_UUID(id) AS id, email FROM user WHERE email = :email")
  Optional<User> findByEmail(@Bind("email") String email);

}
