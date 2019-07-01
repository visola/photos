package org.visola.photos.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.visola.photos.model.PageRequest;
import org.visola.photos.model.Thumbnail;
import org.visola.photos.model.Upload;

public interface ThumbnailDao {

  @SqlUpdate("INSERT INTO thumbnail (id, hash, path, size, mime, created_at, user_id)"
      + " VALUES (:id, :hash, :path, :size, :mime, :createdAt, :userId)")
  void create(@BindBean Thumbnail thumbnail);

  @RegisterBeanMapper(Upload.class)
  @SqlQuery("SELECT u.* FROM upload u" +
      " LEFT OUTER JOIN thumbnail t ON u.id = t.id" +
      " WHERE t.id IS NULL AND u.mime = :mime" +
      " LIMIT :page.size OFFSET :page.offset")
  List<Upload> uploadsWithoutThumbnail(@Bind("mime") String mime, @BindBean("page") PageRequest pageRequest);

}
