
package com.test.spglobal.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.spglobal.component.LastPriceMapper;
import com.test.spglobal.dto.LastPrice;
import com.test.spglobal.records.PricePayload;
import com.test.spglobal.records.PriceRecord;
import com.test.spglobal.services.PriceRecordEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class LastPriceRepository {


    private final JdbcTemplate jdbc;
    private final LastPriceMapper mapper;
    private final ObjectMapper objectMapper;


    public LastPriceRepository(JdbcTemplate jdbc, LastPriceMapper mapper,
                               ObjectMapper objectMapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    public void upsertLastPrice(PriceRecordEvent event) {

        jdbc.update(
                """
                MERGE INTO last_price (instrument_id, as_of, payload)
                KEY (instrument_id)
                VALUES (?, ?, ?)
                """,
                event.instrumentId(),
                Timestamp.from(event.asOf()),
                toJson(event.payload())
        );
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }
    public Optional<LastPrice> findLastPrice(String instrumentId) {
        return jdbc.query(
                """
                SELECT instrument_id, as_of, payload
                FROM last_price
                WHERE instrument_id = ?
                """,
                rs -> {
                    if (!rs.next()) return Optional.empty();

                    try {
                        return Optional.of(
                                new LastPrice(
                                        rs.getString("instrument_id"),
                                        rs.getTimestamp("as_of").toInstant(),
                                        objectMapper.readValue(
                                                rs.getString("payload"),
                                                PricePayload.class
                                        )
                                )
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                },
                instrumentId
        );
    }


}
