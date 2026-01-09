
package com.test.spglobal.servicesImpl;

import com.test.spglobal.records.PriceRecord;
import com.test.spglobal.repository.LastPriceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PriceQueryService {

    private final LastPriceRepository repository;

    public PriceQueryService(LastPriceRepository repository) {
        this.repository = repository;
    }

    public Optional<PriceRecord> getLastPrice(String instrumentId) {
        return null;
    }
}
