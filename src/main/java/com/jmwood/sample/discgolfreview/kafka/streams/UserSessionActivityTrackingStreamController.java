package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.event.AuthEvent;
import com.jmwood.sample.discgolfreview.model.event.ClickEvent;
import com.jmwood.sample.discgolfreview.model.event.CourseEvent;
import com.jmwood.sample.discgolfreview.model.event.NavEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserSessionActivityTrackingStreamController {

    private final KafkaStreamsConfiguration kafkaStreamsConfiguration;

    private KafkaStreams streams;

    private final Topic<String, AuthEvent> authEventTopic;
    private final Topic<String, NavEvent> navEventTopic;
    private final Topic<String, CourseEvent> courseEventTopic;
    private final Topic<String, ClickEvent> clickEventTopic;

    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, AuthEvent> authEventStream = builder
                .stream(authEventTopic.getName(), consumedWith(authEventTopic))
                .peek(this::logConsume);

        KStream<String, NavEvent> navEventStream = builder
                .stream(navEventTopic.getName(), consumedWith(navEventTopic))
                .peek(this::logConsume);

        KStream<String, CourseEvent> courseEventStream = builder
                .stream(courseEventTopic.getName(), consumedWith(courseEventTopic))
                .peek(this::logConsume);

        KStream<String, ClickEvent> clickEventStream = builder
                .stream(clickEventTopic.getName(), consumedWith(clickEventTopic))
                .peek(this::logConsume);


        Topology topology = builder.build();
        return topology;
    }

    @PostConstruct
    final public void start() {
        streams = new KafkaStreams(buildTopology(), kafkaStreamsConfiguration.asProperties());
        streams.start();
    }

    final public void stop() {
        streams.close();
    }

    private <K, V> Consumed<K, V> consumedWith(Topic<K, V> topic) {
        return Consumed.with(topic.getKeySerde(), topic.getValueSerde());
    }

    private <K, V> Produced<K, V> producedWith(Topic<K, V> topic) {
        return Produced.with(topic.getKeySerde(), topic.getValueSerde());
    }

    private void logConsume(Object key, Object value) {
        log.info("Consumed [{}|{}]: {} | {} ", classNameDisplay(key), classNameDisplay(value), key,
                value);
    }

    private void logProduce(Object key, Object value) {
        log.info("Produced [{}|{}]: {} | {} ", classNameDisplay(key), classNameDisplay(value), key,
                value);
    }
    private String classNameDisplay(Object obj) {
        return obj == null ? "null" : obj.getClass().getSimpleName();
    }
}
