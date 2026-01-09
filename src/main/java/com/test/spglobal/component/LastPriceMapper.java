package com.test.spglobal.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.spglobal.dto.LastPrice;
import org.springframework.stereotype.Component;

@Component
public class LastPriceMapper {

    private final ObjectMapper objectMapper;

    public LastPriceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(LastPrice lastPrice) {
        try {
            return objectMapper.writeValueAsString(lastPrice);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize LastPrice", e);
        }
    }
}
