package org.visola.photos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Upload {

  private long id;
  private long userId;
  private String name;
  private String mime;
  private long size;
  private long uploadedAt;

  @JsonIgnore
  private String hash;

  @JsonIgnore
  private String path;

}
