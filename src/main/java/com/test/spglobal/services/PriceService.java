package com.test.spglobal.services;

import com.test.spglobal.records.PriceRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceService {

    UUID startBatch();

    void uploadChunk(UUID batchId, List<PriceRecord> records);

    void completeBatch(UUID batchId);

    void cancelBatch(UUID batchId);

    Optional<PriceRecord> getLastPrice(String instrumentId);
}
