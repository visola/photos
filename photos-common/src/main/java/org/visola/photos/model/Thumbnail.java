package org.visola.photos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Thumbnail {

  private long id;
  private long userId;
  private String mime;
  private long size;
  private long createdAt;

  @JsonIgnore
  private String hash;

  @JsonIgnore
  private String path;

}
