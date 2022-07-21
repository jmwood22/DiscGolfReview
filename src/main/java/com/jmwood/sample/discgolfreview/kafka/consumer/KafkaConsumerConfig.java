package com.jmwood.sample.discgolfreview.kafka.consumer;

import com.jmwood.sample.discgolfreview.model.event.Event;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

  String bootstrapServer = "localhost:9092";

  String groupId = "sample-group-id";

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Event>
      concurrentKafkaListenerContainerFactory(ConsumerFactory<String, Event> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, Event> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setBatchListener(true);
    factory.setConcurrency(1);
    return factory;
  }

  @Bean
  public ConsumerFactory<String, Event> consumerFactory() {
    Map<String, Object> props = new HashMap<>();

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.jmwood.sample.discgolfreview.model.event");

    return new DefaultKafkaConsumerFactory<>(props);
  }
}
