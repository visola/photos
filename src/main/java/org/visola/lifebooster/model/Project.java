package org.visola.lifebooster.model;

import lombok.Data;

@Data
public class Project {

  private long id;
  private String name;
  private long created;
  private long userId;

}
