package com.test.spglobal.services;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BatchStarted.class, name = "BATCH_STARTED"),
        @JsonSubTypes.Type(value = PriceRecordEvent.class, name = "PRICE_RECORD"),
        @JsonSubTypes.Type(value = BatchCompleted.class, name = "BATCH_COMPLETED"),
        @JsonSubTypes.Type(value = BatchCancelled.class, name = "BATCH_CANCELLED")
})
public sealed interface PriceEvent
        permits BatchStarted, PriceRecordEvent, BatchCompleted, BatchCancelled {
}

