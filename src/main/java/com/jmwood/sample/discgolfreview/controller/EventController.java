package com.jmwood.sample.discgolfreview.controller;

import com.jmwood.sample.discgolfreview.model.events.*;
import com.jmwood.sample.discgolfreview.repository.NavEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final KafkaTemplate<String, Event> navEventKafkaTemplate;

    private final NavEventRepository navEventRepository;

    @PostMapping("/auth")
    public ResponseEntity submitAuthEvent(@RequestBody AuthEvent authEvent) {
        log.info("Received request to submit the following AuthEvent for processing: {}", authEvent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/nav")
    public ResponseEntity submitNavEvent(@RequestBody NavEvent navEvent) {
        log.info("Received request to submit the following NavEvent for processing: {}", navEvent);
        NavEvent persistedEvent = navEventRepository.save(navEvent);
        navEventKafkaTemplate.send(navEventKafkaTemplate.getDefaultTopic(), navEvent.getUser().getId(), persistedEvent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/click")
    public ResponseEntity submitClickEvent(@RequestBody ClickEvent clickEvent) {
        log.info("Received request to submit the following ClickEvent for processing: {}", clickEvent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/course")
    public ResponseEntity submitCourseEvent(@RequestBody CourseEvent courseEvent) {
        log.info("Received request to submit the following CourseEvent for processing: {}", courseEvent);
        return ResponseEntity.ok().build();
    }
}
