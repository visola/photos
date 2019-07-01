package org.visola.photos.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PageRequest {

  private final long number;
  private final long size;

  public PageRequest() {
    this.number = 1;
    this.size = 100;
  }

  public long getOffset() {
    return (number - 1) * size;
  }

}
