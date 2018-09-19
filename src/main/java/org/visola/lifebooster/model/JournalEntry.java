package org.visola.lifebooster.model;

import java.util.UUID;

import lombok.Data;

@Data
public class JournalEntry {

  private long id;
  private String entry;
  private long date;
  private UUID userId;

}
