package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.SessionActivity;
import com.jmwood.sample.discgolfreview.model.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("deployed")
public class UserSessionActivityTrackingStreamController {

  private final KafkaStreamsConfiguration kafkaStreamsConfiguration;
  private final AdminClient adminClient;

  private final Topic<String, Event> authEventTopic;
  private final Topic<String, Event> navEventTopic;
  private final Topic<String, Event> courseEventTopic;
  private final Topic<String, Event> clickEventTopic;

  private final Topic<String, SessionActivity> sessionActivityTopic;
  private final SessionActivityAggregator sessionActivityAggregator;
  private KafkaStreams streams;

  public Topology buildTopology() {
    StreamsBuilder builder = new StreamsBuilder();

    KStream<String, Event> authEventStream =
        builder.stream(
                authEventTopic.getName(), consumedWith(authEventTopic, "AUTH_EVENT_INPUT_TOPIC"))
            .peek(this::logConsume);

    KStream<String, Event> navEventStream =
        builder.stream(
                navEventTopic.getName(), consumedWith(navEventTopic, "NAV_EVENT_INPUT_TOPIC"))
            .peek(this::logConsume);

    KStream<String, Event> courseEventStream =
        builder.stream(
                courseEventTopic.getName(),
                consumedWith(courseEventTopic, "COURSE_EVENT_INPUT_TOPIC"))
            .peek(this::logConsume);

    KStream<String, Event> clickEventStream =
        builder.stream(
                clickEventTopic.getName(), consumedWith(clickEventTopic, "CLICK_EVENT_INPUT_TOPIC"))
            .peek(this::logConsume);

    KStream<String, Event> allEvents =
        authEventStream
            .merge(navEventStream)
            .merge(courseEventStream)
            .merge(clickEventStream, Named.as("ALL_EVENTS_STREAM"));

    KTable<String, SessionActivity> sessionActivityKTable =
        allEvents
            .groupByKey()
            .aggregate(
                SessionActivity::new,
                sessionActivityAggregator,
                Materialized.<String, SessionActivity, KeyValueStore<Bytes, byte[]>>as(
                        "Session-Activity")
                    .withKeySerde(sessionActivityTopic.getKeySerde())
                    .withValueSerde(sessionActivityTopic.getValueSerde()));

    sessionActivityKTable
        .toStream()
        .peek(this::logProduce)
        .to(sessionActivityTopic.getName(), producedWith(sessionActivityTopic));

    Topology topology = builder.build();
    log.info("\n{}", topology.describe());
    return topology;
  }

  @PostConstruct
  public final void start() throws ExecutionException, InterruptedException {
    Executors.newSingleThreadExecutor()
        .execute(
            new Runnable() {
              @Override
              public void run() {
                log.info("Attempting to start Kafka Stream processing...");
                streams =
                    new KafkaStreams(buildTopology(), kafkaStreamsConfiguration.asProperties());
                try {
                  waitForAllRequiredTopics();
                } catch (ExecutionException | InterruptedException e) {
                  throw new RuntimeException(e);
                }
                log.info("All required topics found. Starting Kafka Stream processing.");
                streams.start();
              }

              public void waitForAllRequiredTopics()
                  throws ExecutionException, InterruptedException {
                List<String> topicList =
                    List.of(
                        authEventTopic.getName(),
                        navEventTopic.getName(),
                        courseEventTopic.getName(),
                        clickEventTopic.getName());
                log.info("Looking for the following topics to be available: {}", topicList);
                Set<String> listTopicsResult = adminClient.listTopics().names().get();
                log.info("Found the following topics: {}", listTopicsResult);

                boolean inputTopicsCreated = false;
                int sleepTimeMs = 60000;
                while (!inputTopicsCreated) {
                  int foundCount = 0;
                  for (String topic : topicList) {
                    if (!listTopicsResult.contains(topic)) {
                      log.info("Topic {} was not found in set of available topics.", topic);
                    } else {
                      foundCount++;
                    }
                  }
                  if (foundCount == topicList.size()) {
                    inputTopicsCreated = true;
                  } else {
                    log.info(
                        "Found {} of {} required topics. Sleeping for {} seconds before trying again.",
                        foundCount,
                        topicList.size(),
                        sleepTimeMs / 1000);
                    Thread.sleep(sleepTimeMs);
                    listTopicsResult = adminClient.listTopics().names().get();
                  }
                }
              }
            });
  }

  @PreDestroy
  public final void stop() {
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
    log.info(
        "Consumed [{}|{}]: {} | {} ", classNameDisplay(key), classNameDisplay(value), key, value);
  }

  private void logProduce(Object key, Object value) {
    log.info(
        "Produced [{}|{}]: {} | {} ", classNameDisplay(key), classNameDisplay(value), key, value);
  }

  private String classNameDisplay(Object obj) {
    return obj == null ? "null" : obj.getClass().getSimpleName();
  }
}
