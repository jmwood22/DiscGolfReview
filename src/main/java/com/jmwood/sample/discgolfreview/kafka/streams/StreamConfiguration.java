package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.event.AuthEvent;
import com.jmwood.sample.discgolfreview.model.event.ClickEvent;
import com.jmwood.sample.discgolfreview.model.event.CourseEvent;
import com.jmwood.sample.discgolfreview.model.event.NavEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StreamConfiguration {

    @Bean
    KafkaStreamsConfiguration streamsConfiguration(
            @Value("${spring.kafka.streams.application-id}") String applicationId,
            @Value("${spring.kafka.streams.bootstrap-servers}") String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    Topic<String, AuthEvent> authEventTopic(@Value("${kafka.auth.event.topic}") String topicName) {
        return new Topic<String, AuthEvent>(topicName, Serdes.String(), getJsonSerde());
    }

    @Bean
    Topic<String, NavEvent> navEventTopic(@Value("${kafka.nav.event.topic}") String topicName) {
        return new Topic<String, NavEvent>(topicName, Serdes.String(), getJsonSerde());
    }

    @Bean
    Topic<String, CourseEvent> courseEventTopic(@Value("${kafka.course.event.topic}") String topicName) {
        return new Topic<String, CourseEvent>(topicName, Serdes.String(), getJsonSerde());
    }

    @Bean
    Topic<String, ClickEvent> clickEventTopic(@Value("${kafka.click.event.topic}") String topicName) {
        return new Topic<String, ClickEvent>(topicName, Serdes.String(), getJsonSerde());
    }

    private Serde getJsonSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), jsonDeserializer());
    }

    private JsonDeserializer jsonDeserializer() {
        JsonDeserializer jsonDeserializer = new JsonDeserializer<>();
        return jsonDeserializer.trustedPackages("com.jmwood.sample.discgolfreview.model.event");
    }
}
