package org.visola.lifebooster.model;

import java.util.UUID;

import lombok.Data;

@Data
public class Project {

  private long id;
  private String name;
  private long created;
  private UUID userId;

}
