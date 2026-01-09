package com.test.spglobal.services;

import java.util.UUID;

public record BatchCancelled(UUID batchId) implements PriceEvent {
}
