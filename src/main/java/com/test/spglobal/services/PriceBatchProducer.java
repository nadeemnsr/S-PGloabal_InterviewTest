package com.test.spglobal.services;

import com.test.spglobal.records.PriceRecord;

import java.util.List;
import java.util.UUID;

public interface PriceBatchProducer {
    UUID startBatch();
    void uploadChunk(UUID batchId, List<PriceRecord> list);
    void completeBatch(UUID batchId);
    void cancelBatch(UUID batchId);
}
