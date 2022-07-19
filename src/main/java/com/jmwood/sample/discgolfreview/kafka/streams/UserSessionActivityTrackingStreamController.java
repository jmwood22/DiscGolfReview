package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.SessionRollup;
import com.jmwood.sample.discgolfreview.model.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserSessionActivityTrackingStreamController {

    private final KafkaStreamsConfiguration kafkaStreamsConfiguration;

    private KafkaStreams streams;

    private final Topic<String, Event> authEventTopic;
    private final Topic<String, Event> navEventTopic;
    private final Topic<String, Event> courseEventTopic;
    private final Topic<String, Event> clickEventTopic;

    private final SessionAggregator sessionAggregator;

    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, Event> authEventStream = builder
                .stream(authEventTopic.getName(), consumedWith(authEventTopic, "AUTH_EVENT_INPUT_TOPIC"))
                .peek(this::logConsume);

        KStream<String, Event> navEventStream = builder
                .stream(navEventTopic.getName(), consumedWith(navEventTopic, "NAV_EVENT_INPUT_TOPIC"))
                .peek(this::logConsume);

        KStream<String, Event> courseEventStream = builder
                .stream(courseEventTopic.getName(), consumedWith(courseEventTopic, "COURSE_EVENT_INPUT_TOPIC"))
                .peek(this::logConsume);

        KStream<String, Event> clickEventStream = builder
                .stream(clickEventTopic.getName(), consumedWith(clickEventTopic, "CLICK_EVENT_INPUT_TOPIC"))
                .peek(this::logConsume);

        KStream<String, Event> allEvents = authEventStream
                .merge(navEventStream)
                .merge(courseEventStream)
                .merge(clickEventStream, Named.as("ALL_EVENTS_STREAM"));

        Serde<SessionRollup> sessionRollupSerde = Serdes.serdeFrom(new JsonSerializer<SessionRollup>(), new JsonDeserializer<SessionRollup>(SessionRollup.class));

        KTable<String, SessionRollup> sessionRollupKTable = allEvents
                .groupByKey()
                .aggregate(() -> new SessionRollup(new HashMap<>()), sessionAggregator,
                        Materialized.<String, SessionRollup, KeyValueStore<Bytes, byte[]>>as("Session-Rollup")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(sessionRollupSerde));

        sessionRollupKTable.toStream()
                .peek(this::logConsume);

        Topology topology = builder.build();
        log.info("\n{}", topology.describe());
        return topology;
    }

    @PostConstruct
    final public void start() {
        streams = new KafkaStreams(buildTopology(), kafkaStreamsConfiguration.asProperties());
        streams.start();
    }

    @PreDestroy
    final public void stop() {
        log.info("Cleaning up stream");
        streams.cleanUp();
        streams.close();
    }

    private <K, V> Consumed<K, V> consumedWith(Topic<K, V> topic, String name) {
        return Consumed.with(topic.getKeySerde(), topic.getValueSerde()).withName(name);
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
