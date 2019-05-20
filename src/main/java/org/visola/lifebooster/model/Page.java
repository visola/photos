package org.visola.lifebooster.model;

import java.util.List;

import lombok.Data;

@Data
public class Page<T> {

  private List<T> data;
  private int number;
  private int size;
  private long totalElements;

  public int getTotalPages() {
    return (int) Math.ceil( (double) totalElements / (double) size );
  }

}
