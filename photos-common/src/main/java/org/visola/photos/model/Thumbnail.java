package org.visola.photos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Thumbnail {

  private long id;
  private long userId;
  private String hash;
  private String mime;
  private long size;
  private long createdAt;

  @JsonIgnore
  private String path;

}
