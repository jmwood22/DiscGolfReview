package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.SessionActivity;
import com.jmwood.sample.discgolfreview.model.event.Event;
import com.jmwood.sample.discgolfreview.repository.SessionActivityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.Aggregator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class SessionActivityAggregator implements Aggregator<String, Event, SessionActivity> {

  private final SessionActivityRepository sessionActivityRepository;

  @Override
  public SessionActivity apply(String sessionId, Event event, SessionActivity sessionActivity) {

    if (sessionActivity.getId() == null) {
      sessionActivity.setId(sessionId);
      sessionActivity.setUser(event.getUser());
      sessionActivity.setEvents(new ArrayList<>());
    }

    sessionActivity.addEvent(event);

    sessionActivityRepository.save(sessionActivity);

    return sessionActivity;
  }
}
