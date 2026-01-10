package com.test.spglobal.services;

import java.time.Instant;
import java.util.UUID;

public record BatchStarted(UUID batchId, Instant timestamp)
        implements PriceEvent {}
