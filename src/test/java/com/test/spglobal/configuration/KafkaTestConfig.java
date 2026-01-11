package com.test.spglobal.configuration;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

@TestConfiguration
public class KafkaTestConfig {
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = KafkaTestUtils.consumerProps(
                "test-group", "true", embeddedKafkaBroker);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>()
        );
    }
}
