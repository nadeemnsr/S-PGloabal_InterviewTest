
package com.test.spglobal.repository;

import com.test.spglobal.component.LastPriceMapper;
import com.test.spglobal.dto.LastPrice;
import com.test.spglobal.services.PriceRecordEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LastPriceRepository {


    private final JdbcTemplate jdbc;
    private final LastPriceMapper mapper;

    public LastPriceRepository(JdbcTemplate jdbc, LastPriceMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public void upsertLastPrice(PriceRecordEvent e) {
        jdbc.update("""
            INSERT INTO last_price (instrument_id, as_of, payload)
            VALUES (?, ?, ?::jsonb)
            ON CONFLICT (instrument_id)
            DO UPDATE SET
              as_of = EXCLUDED.as_of,
              payload = EXCLUDED.payload
            WHERE EXCLUDED.as_of > last_price.as_of
        """,
        e.instrumentId(),
        e.asOf(), mapper.toJson((LastPrice) e.payload())
        );
    }

}
