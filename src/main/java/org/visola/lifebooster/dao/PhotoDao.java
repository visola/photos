package org.visola.lifebooster.dao;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.visola.lifebooster.model.Photo;

public interface PhotoDao {

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO photo (hash, name, path, size, uploaded_at)"
      + " VALUES (:hash, :name, :path, :size, :uploadedAt)")
  long create(@BindBean Photo photo);

  @RegisterBeanMapper(Photo.class)
  @SqlQuery("SELECT * FROM photo WHERE hash = :hash AND user_id := userId")
  Optional<Photo> findByHash(@Bind("hash") String hash, @Bind("userId") long userId);


}
