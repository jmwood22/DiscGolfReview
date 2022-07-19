package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.SessionActivity;
import com.jmwood.sample.discgolfreview.model.SessionRollup;
import com.jmwood.sample.discgolfreview.model.event.Event;
import com.jmwood.sample.discgolfreview.repository.SessionActivityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.Aggregator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SessionAggregator implements Aggregator<String, Event, SessionRollup> {

    private final SessionActivityRepository sessionActivityRepository;

    @Override
    public SessionRollup apply(String sessionId, Event event, SessionRollup sessionRollup) {

        final Map<String, SessionActivity> allSessionActivities = sessionRollup.getSessionActivityMap();
        final SessionActivity sessionActivity = allSessionActivities.computeIfAbsent(sessionId, k -> new SessionActivity(sessionId, event.getUser(), new ArrayList<>()));
        sessionActivity.addEvent(event);

        sessionActivityRepository.save(sessionActivity);

        return sessionRollup;
    }
}
