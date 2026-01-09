package com.test.spglobal.dto;

import com.test.spglobal.services.PriceRecordEvent;

import java.util.HashMap;
import java.util.Map;

public class BatchState {
    public final Map<String, PriceRecordEvent> latestPrices = new HashMap<>();
    boolean completed = false;
}
