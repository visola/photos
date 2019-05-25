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

  @RegisterBeanMapper(Photo.class)
  @SqlQuery("SELECT COUNT(1) FROM photo WHERE user_id = :userId")
  long countPhotos(@Bind("userId") long userId);

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO photo (hash, name, path, size, uploaded_at, user_id)"
      + " VALUES (:hash, :name, :path, :size, :uploadedAt, :userId)")
  long create(@BindBean Photo photo);

  @RegisterBeanMapper(Photo.class)
  @SqlQuery("SELECT *"
      + " FROM photo"
      + " WHERE user_id = :userId"
      + " ORDER BY uploaded_at DESC"
      + " LIMIT :pageSize"
      + " OFFSET :offset")
  List<Photo> fetchPage(
      @Bind("userId") long userId,
      @Bind("offset") long offset,
      @Bind("pageSize") int pageSize
  );

  @RegisterBeanMapper(Photo.class)
  @SqlQuery("SELECT * FROM photo WHERE hash = :hash AND user_id = :userId")
  Optional<Photo> findByHash(@Bind("hash") String hash, @Bind("userId") long userId);

  @RegisterBeanMapper(Photo.class)
  @SqlQuery("SELECT * FROM photo WHERE id = :id AND user_id = :userId")
  Optional<Photo> findById(@Bind("id") long id, @Bind("userId") long userId);

}
