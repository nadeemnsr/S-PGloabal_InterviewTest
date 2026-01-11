package com.test.spglobal.controller;

import com.test.spglobal.records.PriceRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(topics = "price-batch-events")
class PriceFlowIT {

    @Autowired
    TestRestTemplate rest;

    @Test
    void endToEndFlow() throws Exception {
        UUID batchId = rest.postForObject(
                "/api/prices/batch/start",
                null,
                UUID.class
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<PriceRecord>> request =
                new HttpEntity<>(
                        List.of(
                                new PriceRecord(
                                        "AAPL",
                                        Instant.parse("2026-01-08T10:00:00Z"),
                                        Map.of("price", 180)
                                )
                        ),
                        headers
                );

        rest.postForEntity(
                "http://localhost:8080/api/prices/batch/" + batchId + "/upload",
                request,
                Void.class
        );

        rest.postForEntity(
                "/api/prices/batch/" + batchId + "/complete",
                null,
                Void.class
        );

        Thread.sleep(1000);

        ResponseEntity<PriceRecord> response =
                rest.getForEntity("/api/prices/AAPL", PriceRecord.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
