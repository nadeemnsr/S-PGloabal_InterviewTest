package com.test.spglobal.records;

import java.time.Instant;
import java.util.Map;

public record PriceRecord(
        String id,
        Instant asOf,
        Map<String, Object> payload
) {}
