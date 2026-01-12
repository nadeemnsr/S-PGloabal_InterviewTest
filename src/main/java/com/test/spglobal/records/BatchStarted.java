package com.test.spglobal.records;

import com.test.spglobal.records.PriceEvent;

import java.time.Instant;
import java.util.UUID;

public record BatchStarted(UUID batchId, Instant timestamp)
        implements PriceEvent {}
