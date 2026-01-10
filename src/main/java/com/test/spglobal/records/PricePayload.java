package com.test.spglobal.records;

import java.math.BigDecimal;

public record PricePayload(
    BigDecimal price,
    String currency
) {}
