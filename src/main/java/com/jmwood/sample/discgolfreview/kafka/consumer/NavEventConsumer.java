package com.jmwood.sample.discgolfreview.kafka.consumer;

import com.jmwood.sample.discgolfreview.model.events.NavEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NavEventConsumer {

    @KafkaListener(
            topics = "#{'${kafka.nav.event.topic}'.split(',')}",
            groupId = "${kafka.nav.event.group}",
            containerFactory = "concurrentKafkaListenerContainerFactory",
            id = "navEventConsumer"
    )
    public void listen(@Payload NavEvent navEvent) {
        log.info("Received navEvent: {}", navEvent.toString());
    }
}
