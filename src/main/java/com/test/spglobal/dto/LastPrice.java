package com.test.spglobal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastPrice {

    private String id;
    private Instant asOf;
    private Object payload; // flexible structure

    public LastPrice() {
    }

    public LastPrice(String id, Instant asOf, Object payload) {
        this.id = id;
        this.asOf = asOf;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public Instant getAsOf() {
        return asOf;
    }

    public Object getPayload() {
        return payload;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAsOf(Instant asOf) {
        this.asOf = asOf;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
