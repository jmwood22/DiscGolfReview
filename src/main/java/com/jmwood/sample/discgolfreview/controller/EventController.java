package com.jmwood.sample.discgolfreview.controller;

import com.jmwood.sample.discgolfreview.model.event.*;
import com.jmwood.sample.discgolfreview.repository.event.AuthEventRepository;
import com.jmwood.sample.discgolfreview.repository.event.ClickEventRepository;
import com.jmwood.sample.discgolfreview.repository.event.CourseEventRepository;
import com.jmwood.sample.discgolfreview.repository.event.NavEventRepository;
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
  private final KafkaTemplate<String, Event> clickEventKafkaTemplate;
  private final KafkaTemplate<String, Event> courseEventKafkaTemplate;
  private final KafkaTemplate<String, Event> authEventKafkaTemplate;

  private final NavEventRepository navEventRepository;
  private final ClickEventRepository clickEventRepository;
  private final CourseEventRepository courseEventRepository;
  private final AuthEventRepository authEventRepository;

  @PostMapping("/auth")
  public ResponseEntity submitAuthEvent(@RequestBody AuthEvent authEvent) {
    log.info("Received request to submit the following AuthEvent for processing: {}", authEvent);
    AuthEvent persistedEvent = authEventRepository.save(authEvent);
    authEventKafkaTemplate.send(
        authEventKafkaTemplate.getDefaultTopic(), authEvent.getSessionId(), persistedEvent);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/nav")
  public ResponseEntity submitNavEvent(@RequestBody NavEvent navEvent) {
    log.info("Received request to submit the following NavEvent for processing: {}", navEvent);
    NavEvent persistedEvent = navEventRepository.save(navEvent);
    navEventKafkaTemplate.send(
        navEventKafkaTemplate.getDefaultTopic(), navEvent.getSessionId(), persistedEvent);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/click")
  public ResponseEntity submitClickEvent(@RequestBody ClickEvent clickEvent) {
    log.info("Received request to submit the following ClickEvent for processing: {}", clickEvent);
    ClickEvent persistedEvent = clickEventRepository.save(clickEvent);
    clickEventKafkaTemplate.send(
        clickEventKafkaTemplate.getDefaultTopic(), clickEvent.getSessionId(), persistedEvent);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/course")
  public ResponseEntity submitCourseEvent(@RequestBody CourseEvent courseEvent) {
    log.info(
        "Received request to submit the following CourseEvent for processing: {}", courseEvent);
    CourseEvent persistedEvent = courseEventRepository.save(courseEvent);
    courseEventKafkaTemplate.send(
        courseEventKafkaTemplate.getDefaultTopic(), courseEvent.getSessionId(), persistedEvent);
    return ResponseEntity.ok().build();
  }
}
