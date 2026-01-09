package com.test.spglobal.services;

import java.util.UUID;

public sealed interface PriceEvent
        permits BatchStarted, PriceRecordEvent,
                BatchCompleted, BatchCancelled {

    UUID batchId();
}
