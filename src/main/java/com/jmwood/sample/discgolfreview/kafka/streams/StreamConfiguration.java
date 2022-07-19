package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.event.Event;
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
    Topic<String, Event> authEventTopic(@Value("${kafka.auth.event.topic}") String topicName) {
        return new Topic<String, Event>(topicName, Serdes.String(), getJsonSerde());
    }

    @Bean
    Topic<String, Event> navEventTopic(@Value("${kafka.nav.event.topic}") String topicName) {
        return new Topic<String, Event>(topicName, Serdes.String(), getJsonSerde());
    }

    @Bean
    Topic<String, Event> courseEventTopic(@Value("${kafka.course.event.topic}") String topicName) {
        return new Topic<String, Event>(topicName, Serdes.String(), getJsonSerde());
    }

    @Bean
    Topic<String, Event> clickEventTopic(@Value("${kafka.click.event.topic}") String topicName) {
        return new Topic<String, Event>(topicName, Serdes.String(), getJsonSerde());
    }

    public static Serde<Event> getJsonSerde() {
        return Serdes.serdeFrom(new JsonSerializer<Event>(), jsonDeserializer());
    }

    private static JsonDeserializer<Event> jsonDeserializer() {
        JsonDeserializer<Event> jsonDeserializer = new JsonDeserializer<Event>();
        return jsonDeserializer.trustedPackages("com.jmwood.sample.discgolfreview.model.event");
    }
}
