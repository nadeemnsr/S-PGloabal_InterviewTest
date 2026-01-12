package com.test.spglobal.records;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record PriceRecordEvent(
        UUID batchId,
        String instrumentId,
        Instant asOf,
        Map<String, Object> payload
) implements PriceEvent {}
