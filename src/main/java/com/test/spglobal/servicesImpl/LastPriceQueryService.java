package com.test.spglobal.servicesImpl;

import com.test.spglobal.dto.LastPrice;
import com.test.spglobal.repository.LastPriceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LastPriceQueryService {

    private final LastPriceRepository repository;

    public LastPriceQueryService(LastPriceRepository repository) {
        this.repository = repository;
    }

    public Optional<LastPrice> getLastPrice(String instrumentId) {
        return repository.findLastPrice(instrumentId);
    }
}
