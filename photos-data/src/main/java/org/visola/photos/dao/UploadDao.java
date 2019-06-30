package org.visola.photos.dao;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.visola.photos.model.Upload;

public interface UploadDao {

  @RegisterBeanMapper(Upload.class)
  @SqlQuery("SELECT COUNT(1) FROM upload WHERE user_id = :userId")
  long countUploads(@Bind("userId") long userId);

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO upload (hash, name, path, size, mime, uploaded_at, user_id)"
      + " VALUES (:hash, :name, :path, :size, :mime, :uploadedAt, :userId)")
  long create(@BindBean Upload upload);

  @RegisterBeanMapper(Upload.class)
  @SqlQuery("SELECT *"
      + " FROM upload"
      + " WHERE user_id = :userId"
      + " ORDER BY uploaded_at DESC"
      + " LIMIT :pageSize"
      + " OFFSET :offset")
  List<Upload> fetchPageByUserId(
      @Bind("userId") long userId,
      @Bind("offset") long offset,
      @Bind("pageSize") int pageSize
  );

  @RegisterBeanMapper(Upload.class)
  @SqlQuery("SELECT * FROM upload WHERE hash = :hash AND user_id = :userId")
  Optional<Upload> findByHash(@Bind("hash") String hash, @Bind("userId") long userId);

  @RegisterBeanMapper(Upload.class)
  @SqlQuery("SELECT * FROM upload WHERE id = :id AND user_id = :userId")
  Optional<Upload> findById(@Bind("id") long id, @Bind("userId") long userId);

}
