package com.test.spglobal.records;

import com.test.spglobal.records.PriceEvent;

import java.util.UUID;

public record BatchCancelled(UUID batchId) implements PriceEvent {
}
