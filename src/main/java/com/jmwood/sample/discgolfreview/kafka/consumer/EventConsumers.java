package com.jmwood.sample.discgolfreview.kafka.consumer;

import com.jmwood.sample.discgolfreview.model.event.AuthEvent;
import com.jmwood.sample.discgolfreview.model.event.ClickEvent;
import com.jmwood.sample.discgolfreview.model.event.CourseEvent;
import com.jmwood.sample.discgolfreview.model.event.NavEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventConsumers {

  @KafkaListener(
      topics = "#{'${kafka.nav.event.topic}'.split(',')}",
      groupId = "${kafka.nav.event.group}",
      containerFactory = "concurrentKafkaListenerContainerFactory",
      id = "navEventConsumer")
  public void listenToNavEvents(@Payload NavEvent navEvent) {
    log.info("Received NavEvent: {}", navEvent.toString());
  }

  @KafkaListener(
      topics = "#{'${kafka.auth.event.topic}'.split(',')}",
      groupId = "${kafka.auth.event.group}",
      containerFactory = "concurrentKafkaListenerContainerFactory",
      id = "authEventConsumer")
  public void listenToAuthEvents(@Payload AuthEvent authEvent) {
    log.info("Received AuthEvent: {}", authEvent.toString());
  }

  @KafkaListener(
      topics = "#{'${kafka.course.event.topic}'.split(',')}",
      groupId = "${kafka.course.event.group}",
      containerFactory = "concurrentKafkaListenerContainerFactory",
      id = "courseEventConsumer")
  public void listenToCourseEvents(@Payload CourseEvent courseEvent) {
    log.info("Received CourseEvent: {}", courseEvent.toString());
  }

  @KafkaListener(
      topics = "#{'${kafka.click.event.topic}'.split(',')}",
      groupId = "${kafka.click.event.group}",
      containerFactory = "concurrentKafkaListenerContainerFactory",
      id = "clickEventConsumer")
  public void listenToClickEvents(@Payload ClickEvent clickEvent) {
    log.info("Received ClickEvent: {}", clickEvent.toString());
  }
}
