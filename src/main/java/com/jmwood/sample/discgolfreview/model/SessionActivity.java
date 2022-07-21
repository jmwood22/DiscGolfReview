package com.jmwood.sample.discgolfreview.model;

import com.jmwood.sample.discgolfreview.model.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sessionActivity")
public class SessionActivity implements Serializable {

  @Id private String id;
  private User user;
  private List<Event> events;

  public void addEvent(Event event) {
    events.add(event);
  }
}
