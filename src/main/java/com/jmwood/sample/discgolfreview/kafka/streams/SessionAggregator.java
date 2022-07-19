package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.SessionActivity;
import com.jmwood.sample.discgolfreview.model.SessionRollup;
import com.jmwood.sample.discgolfreview.model.event.Event;
import org.apache.kafka.streams.kstream.Aggregator;

import java.util.ArrayList;
import java.util.Map;

public class SessionAggregator implements Aggregator<String, Event, SessionRollup> {

    @Override
    public SessionRollup apply(String sessionId, Event event, SessionRollup sessionRollup) {

        final Map<String, SessionActivity> allSessionActivities = sessionRollup.getSessionActivityMap();
        final SessionActivity sessionActivity = allSessionActivities.computeIfAbsent(sessionId, k -> new SessionActivity(sessionId, event.getUser(), new ArrayList<>()));
        sessionActivity.addEvent(event);

        return sessionRollup;
    }
}
