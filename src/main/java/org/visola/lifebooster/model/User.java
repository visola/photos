package org.visola.lifebooster.model;

import java.util.UUID;

import lombok.Data;

@Data
public class User {

  private UUID id;
  private String email;

}
