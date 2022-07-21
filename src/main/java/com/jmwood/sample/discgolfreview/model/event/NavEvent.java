package com.jmwood.sample.discgolfreview.model.event;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Jacksonized
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "NavEvents")
public class NavEvent extends Event {

  private String path;
  private String rawLocationJson;
}
