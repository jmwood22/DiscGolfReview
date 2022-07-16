package com.jmwood.sample.discgolfreview.kafka.producer;

import com.jmwood.sample.discgolfreview.model.event.Event;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    String bootstrapAddress = "localhost:9092";

    String navTopic = "dgr-nav-events";
    String clickTopic = "dgr-click-events";
    String courseTopic = "dgr-course-events";
    String authTopic = "dgr-auth-events";

    @Bean
    public ProducerFactory<String, Event> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Event> navEventKafkaTemplate(ProducerFactory<String, Event> producerFactory) {
        KafkaTemplate<String, Event> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(navTopic);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, Event> clickEventKafkaTemplate(ProducerFactory<String, Event> producerFactory) {
        KafkaTemplate<String, Event> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(clickTopic);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, Event> courseEventKafkaTemplate(ProducerFactory<String, Event> producerFactory) {
        KafkaTemplate<String, Event> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(courseTopic);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, Event> authEventKafkaTemplate(ProducerFactory<String, Event> producerFactory) {
        KafkaTemplate<String, Event> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(authTopic);
        return kafkaTemplate;
    }
}
