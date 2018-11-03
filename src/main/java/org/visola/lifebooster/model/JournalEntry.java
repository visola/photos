package org.visola.lifebooster.model;

import lombok.Data;

@Data
public class JournalEntry {

  private long id;
  private String entry;
  private long date;
  private long userId;

}
