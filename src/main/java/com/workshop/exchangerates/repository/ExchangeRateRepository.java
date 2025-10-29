package com.workshop.exchangerates.repository;

import com.workshop.exchangerates.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    
    List<ExchangeRate> findByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency);
    
    List<ExchangeRate> findByBaseCurrencyAndTargetCurrencyAndRateDateAfter(
        String baseCurrency, String targetCurrency, LocalDateTime date);
    
    List<ExchangeRate> findBySource(String source);
}
