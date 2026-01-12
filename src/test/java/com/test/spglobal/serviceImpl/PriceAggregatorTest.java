package com.test.spglobal.serviceImpl;

import com.test.spglobal.records.BatchCancelled;
import com.test.spglobal.records.BatchCompleted;
import com.test.spglobal.records.BatchStarted;
import com.test.spglobal.records.PriceRecordEvent;
import com.test.spglobal.repository.LastPriceRepository;
import com.test.spglobal.servicesImpl.PriceAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PriceAggregatorTest {

    private PriceAggregator aggregator;
    private LastPriceRepository repository;

    @BeforeEach
    void setup() {
        repository = mock(LastPriceRepository.class);
        aggregator = new PriceAggregator(repository);
    }

    @Test
    void shouldIgnorePriceIfBatchNotStarted() {
        UUID batchId = UUID.randomUUID();

        aggregator.handlePriceEvent(
                new PriceRecordEvent(
                        batchId,
                        "MSFT",
                        Instant.now(),
                        Map.of("price", 300)
                )
        );

        aggregator.handlePriceEvent(
                new BatchCompleted(batchId)
        );

        verify(repository, never()).upsertLastPrice(any());
    }


    @Test
    void shouldStoreLatestPriceByAsOf() {
        UUID batchId = UUID.randomUUID();

        aggregator.handlePriceEvent(
                new BatchStarted(batchId, Instant.now())
        );

        aggregator.handlePriceEvent(
                new PriceRecordEvent(
                        batchId,
                        "AAPL",
                        Instant.parse("2026-01-08T10:00:00Z"),
                        Map.of("price", 180)
                )
        );

        aggregator.handlePriceEvent(
                new PriceRecordEvent(
                        batchId,
                        "AAPL",
                        Instant.parse("2026-01-08T10:05:00Z"),
                        Map.of("price", 190)
                )
        );

        aggregator.handlePriceEvent(
                new BatchCompleted(batchId)
        );

        ArgumentCaptor<PriceRecordEvent> captor =
                ArgumentCaptor.forClass(PriceRecordEvent.class);

        verify(repository).upsertLastPrice(captor.capture());

        assertEquals("AAPL", captor.getValue().instrumentId());
        assertEquals(190, captor.getValue().payload().get("price"));
    }

    @Test
    void shouldIgnoreOlderPrice() {
        UUID batchId = UUID.randomUUID();

        aggregator.handlePriceEvent(new BatchStarted(batchId, Instant.now()));

        aggregator.handlePriceEvent(
                new PriceRecordEvent(
                        batchId,
                        "GOOG",
                        Instant.parse("2026-01-08T10:05:00Z"),
                        Map.of("price", 2700)
                )
        );

        aggregator.handlePriceEvent(
                new PriceRecordEvent(
                        batchId,
                        "GOOG",
                        Instant.parse("2026-01-08T10:00:00Z"),
                        Map.of("price", 2600)
                )
        );

        aggregator.handlePriceEvent(new BatchCompleted(batchId));

        ArgumentCaptor<PriceRecordEvent> captor =
                ArgumentCaptor.forClass(PriceRecordEvent.class);

        verify(repository).upsertLastPrice(captor.capture());

        assertEquals(2700, captor.getValue().payload().get("price"));
    }

    @Test
    void shouldHandlePriceBeforeBatchStartedSafely() {
        UUID batchId = UUID.randomUUID();

        // Price comes BEFORE BatchStarted
        aggregator.handlePriceEvent(
                new PriceRecordEvent(
                        batchId,
                        "MSFT",
                        Instant.now(),
                        Map.of("price", 300)
                )
        );

        // Should not crash
        aggregator.handlePriceEvent(
                new BatchCompleted(batchId)
        );

        verify(repository, never()).upsertLastPrice(any());
    }

    @Test
    void shouldDiscardBatchOnCancel() {
        UUID batchId = UUID.randomUUID();

        aggregator.handlePriceEvent(new BatchStarted(batchId, Instant.now()));

        aggregator.handlePriceEvent(
                new PriceRecordEvent(
                        batchId,
                        "NFLX",
                        Instant.now(),
                        Map.of("price", 500)
                )
        );

        aggregator.handlePriceEvent(new BatchCancelled(batchId));

        // Completing after cancel should do nothing
        aggregator.handlePriceEvent(new BatchCompleted(batchId));

        verify(repository, never()).upsertLastPrice(any());
    }
}
