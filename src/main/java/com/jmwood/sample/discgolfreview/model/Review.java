package com.jmwood.sample.discgolfreview.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class Review implements Serializable {

  @Id private String id;
  private User author;
  private String text;
  private Integer rating;
}
