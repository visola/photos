package org.visola.photos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Photo {

  private long id;
  private long userId;
  private String hash;
  private String name;
  private long size;
  private long uploadedAt;

  @JsonIgnore
  private String path;

}
