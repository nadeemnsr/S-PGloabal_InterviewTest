package com.test.spglobal.controller;


import com.test.spglobal.records.PriceRecord;
import com.test.spglobal.servicesImpl.KafkaPriceBatchProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final KafkaPriceBatchProducer kafkaPriceBatchProducer;

    public PriceController(KafkaPriceBatchProducer kafkaPriceBatchProducer) {
        this.kafkaPriceBatchProducer = kafkaPriceBatchProducer;
    }

    // 1️⃣ Start batch (Kafka event)
    @PostMapping("/batch/start")
    public ResponseEntity<UUID> startBatch() {
        UUID batchId = kafkaPriceBatchProducer.startBatch();
        return ResponseEntity.ok(batchId);
    }

    // 2️⃣ Upload chunk → PRODUCE to Kafka
    @PostMapping("/batch/{batchId}/upload")
    public ResponseEntity<Void> uploadChunk(
            @PathVariable UUID batchId,
            @RequestBody List<PriceRecord> records) {

        if (records == null || records.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (records.size() > 1000) {
            return ResponseEntity
                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .build();
        }

        kafkaPriceBatchProducer.uploadChunk(batchId, records);
        return ResponseEntity.accepted().build();
    }

    // 3️⃣ Complete batch → COMMIT event
    @PostMapping("/batch/{batchId}/complete")
    public ResponseEntity<Void> completeBatch(@PathVariable UUID batchId) {
        kafkaPriceBatchProducer.completeBatch(batchId);
        return ResponseEntity.ok().build();
    }

    // 4️⃣ Cancel batch → DISCARD event
    @PostMapping("/batch/{batchId}/cancel")
    public ResponseEntity<Void> cancelBatch(@PathVariable UUID batchId) {
        kafkaPriceBatchProducer.cancelBatch(batchId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{instrumentId}")
    public ResponseEntity<PriceRecord> getLastPrice(
            @PathVariable String instrumentId) {

        Optional<PriceRecord> price =
                priceService.getLastPrice(instrumentId);

        return price
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

