package com.test.spglobal.servicesImpl;

import com.test.spglobal.records.PriceRecord;
import com.test.spglobal.services.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class KafkaPriceBatchProducer implements PriceBatchProducer {

    private final KafkaTemplate<String, PriceEvent> kafkaTemplate;
    private static final String TOPIC = "price-batch-events";

    public KafkaPriceBatchProducer(KafkaTemplate<String , PriceEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public UUID startBatch() {
        UUID batchId = UUID.randomUUID();
        kafkaTemplate.send(TOPIC,batchId.toString(),
                new BatchStarted(batchId, Instant.now()));
        return batchId;
    }

    @Override
    public void uploadChunk(UUID batchId, List<PriceRecord> records) {
        records.forEach(r ->
            kafkaTemplate.send(TOPIC, batchId.toString(),
                new PriceRecordEvent(
                        batchId,
                        r.id(),
                        r.asOf(),
                        r.payload()
                )
            )
        );
    }

    @Override
    public void completeBatch(UUID batchId) {
        kafkaTemplate.send(TOPIC, batchId.toString(),
                new BatchCompleted(batchId));
    }

    @Override
    public void cancelBatch(UUID batchId) {
        kafkaTemplate.send(TOPIC, batchId.toString(),
                new BatchCancelled(batchId));
    }
}
