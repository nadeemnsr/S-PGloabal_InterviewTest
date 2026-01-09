package com.test.spglobal.servicesImpl;

import com.test.spglobal.dto.BatchState;
import com.test.spglobal.repository.LastPriceRepository;
import com.test.spglobal.services.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceAggregator {

    private final Map<UUID, BatchState> batches = new ConcurrentHashMap<>();
    private final LastPriceRepository repository;

    public PriceAggregator(LastPriceRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "price-batch-events",
            groupId = "price-aggregator"
    )
    @Transactional
    public void onEvent(PriceEvent event) {

        if (event instanceof BatchStarted e) {
            batches.put(e.batchId(), new BatchState());
        }

        else if (event instanceof PriceRecordEvent e) {
            BatchState batch = getBatch(e.batchId());

            batch.latestPrices.merge(
                    e.instrumentId(),
                    e,
                    (oldVal, newVal) ->
                            newVal.asOf().isAfter(oldVal.asOf())
                                    ? newVal
                                    : oldVal
            );
        }

        else if (event instanceof BatchCompleted e) {
            BatchState batch = getBatch(e.batchId());

            // Atomic DB publish
            batch.latestPrices.values()
                    .forEach(repository::upsertLastPrice);

            batches.remove(e.batchId());
        }

        else if (event instanceof BatchCancelled e) {
            batches.remove(e.batchId());
        }
    }

    private BatchState getBatch(UUID batchId) {
        BatchState batch = batches.get(batchId);
        if (batch == null) {
            throw new IllegalStateException("Invalid batch: " + batchId);
        }
        return batch;
    }
}
