## Architecture Overview
Client (curl / REST)
        |
        v
Batch REST API
        |
        v
Kafka (price-batch-events)
        |
        v
PriceAggregator
        |
        v
H2 Database
        |
        v
Query REST API

## Project Structure
src/main/java/com/test/spglobal
│
├── controller
│   ├── PriceBatchController.java
│   └── LastPriceQueryController.java
│
├── services
│   ├── PriceEvent.java
│   ├── BatchStarted.java
│   ├── PriceRecordEvent.java
│   ├── BatchCompleted.java
│   └── BatchCancelled.java
│
├── servicesImpl
│   └── PriceAggregator.java
│
├── repository
│   └── LastPriceRepository.java
│
├── dto
│   ├── LastPrice.java
│   ├── PricePayload.java
│   └── BatchState.java
│
└── config
    └── KafkaConfig.java


## API Endpoints

POST /api/prices/batch/start
# Response 
{
  "batchId": "ca99605d-afda-4c7f-95bc-c647c74ffd51"
}

# Upload Prices

POST /api/prices/batch/{batchId}/upload

# Response 

[
  {
    "id": "AAPL",
    "asOf": "2026-01-08T10:30:00",
    "payload": {
      "price": 189.25,
      "currency": "USD"
    }
  },
  {
    "id": "GOOG",
    "asOf": "2026-01-08T10:31:00",
    "payload": {
      "price": 2745.10,
      "currency": "USD"
    }
  }
]

# Complete Batch

POST /api/prices/batch/{batchId}/complete


# Last Price 
curl http://localhost:8080/api/prices/AAPL

# Response 
{
  "id": "AAPL",
  "asOf": "2026-01-08T10:30:00",
  "payload": {
    "price": 189.25,
    "currency": "USD"
  }
}


## Database Schema

CREATE TABLE last_price (
  instrument_id VARCHAR(20) PRIMARY KEY,
  as_of TIMESTAMP NOT NULL,
  payload CLOB NOT NULL
);


## Local Setup

#Prerequisites

Java 17+

Kafka (local or Docker)

Maven


## Run app
mvn spring-boot:run


## h2-console
http://localhost:8080/h2-console
#JDBC URL
jdbc:h2:mem:testdb


