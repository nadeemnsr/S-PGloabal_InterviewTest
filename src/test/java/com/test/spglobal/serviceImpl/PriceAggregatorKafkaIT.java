package com.test.spglobal.serviceImpl;

import com.test.spglobal.repository.LastPriceRepository;
import com.test.spglobal.services.BatchCompleted;
import com.test.spglobal.services.BatchStarted;
import com.test.spglobal.services.PriceRecordEvent;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@EmbeddedKafka(
        topics = "price-batch-events",
        partitions = 1
)
class PriceAggregatorKafkaIT {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private LastPriceRepository repository;

    PriceAggregatorKafkaIT() {
    }

    @Test
    void shouldConsumeKafkaEventsEndToEnd() {
        UUID batchId = UUID.randomUUID();

        kafkaTemplate.send("price-batch-events",
                new BatchStarted(batchId, Instant.now()));

        kafkaTemplate.send("price-batch-events",
                new PriceRecordEvent(
                        batchId,
                        "AAPL",
                        Instant.now(),
                        Map.of("price", 180)));

        kafkaTemplate.send("price-batch-events",
                new BatchCompleted(batchId));

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .until(() ->
                        repository.findLastPrice("AAPL").isPresent()
                );
    }
}
