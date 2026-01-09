package com.test.spglobal.services;

import java.util.UUID;

public record BatchCompleted(UUID batchId) implements PriceEvent {}
