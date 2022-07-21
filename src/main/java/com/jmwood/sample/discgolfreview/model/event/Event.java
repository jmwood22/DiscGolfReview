package com.jmwood.sample.discgolfreview.model.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jmwood.sample.discgolfreview.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

// @Data
@Getter
@Setter
@ToString
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
  @JsonSubTypes.Type(value = AuthEvent.class, name = "@class"),
  @JsonSubTypes.Type(value = NavEvent.class, name = "@class"),
  @JsonSubTypes.Type(value = CourseEvent.class, name = "@class"),
  @JsonSubTypes.Type(value = ClickEvent.class, name = "@class")
})
public abstract class Event implements Serializable {

  @Id private String id;
  private String sessionId;
  private User user;
  private Date date;
}
