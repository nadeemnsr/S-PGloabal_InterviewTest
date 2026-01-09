package com.test.spglobal.dto;

import java.time.Instant;
import java.util.UUID;

public class BatchStatus {

    private UUID batchId;
    private String status; // STARTED, COMPLETED, CANCELLED
    private Instant startedAt;
    private Instant completedAt;
    private int totalRecords;

    public BatchStatus(UUID batchId, String status,
                       Instant startedAt, Instant completedAt,
                       int totalRecords) {
        this.batchId = batchId;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.totalRecords = totalRecords;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public int getTotalRecords() {
        return totalRecords;
    }
}
