CREATE TABLE IF NOT EXISTS last_price (
    instrument_id VARCHAR(50) PRIMARY KEY,
    as_of TIMESTAMP,
    payload CLOB
);
