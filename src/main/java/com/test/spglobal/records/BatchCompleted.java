package com.test.spglobal.records;

import com.test.spglobal.records.PriceEvent;

import java.util.UUID;

public record BatchCompleted(UUID batchId) implements PriceEvent {}
