package org.visola.lifebooster.model;

import lombok.Data;

@Data
public class Photo {

  private long id;
  private long userId;
  private String hash;
  private String name;
  private String path;
  private long size;
  private long uploadedAt;

}
