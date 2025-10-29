package com.workshop.exchangerates.service;

import com.workshop.exchangerates.model.ExchangeRate;
import com.workshop.exchangerates.repository.ExchangeRateRepository;
import com.workshop.exchangerates.service.provider.ExchangeRateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private final ExchangeRateRepository repository;
    private final List<ExchangeRateProvider> providers;

    public ExchangeRateService(ExchangeRateRepository repository, List<ExchangeRateProvider> providers) {
        this.repository = repository;
        this.providers = providers;
    }

    @Transactional
    public void fetchAndStoreRates(String baseCurrency) {
        logger.info("Fetching exchange rates for base currency: {}", baseCurrency);
        
        for (ExchangeRateProvider provider : providers) {
            try {
                Map<String, BigDecimal> rates = provider.getExchangeRates(baseCurrency);
                
                if (rates.isEmpty()) {
                    logger.warn("No rates received from provider: {}", provider.getProviderName());
                    continue;
                }
                
                LocalDateTime rateDate = LocalDateTime.now();
                
                for (Map.Entry<String, BigDecimal> entry : rates.entrySet()) {
                    ExchangeRate exchangeRate = new ExchangeRate(
                        baseCurrency,
                        entry.getKey(),
                        entry.getValue(),
                        rateDate,
                        provider.getProviderName()
                    );
                    
                    repository.save(exchangeRate);
                }
                
                logger.info("Saved {} rates from {}", rates.size(), provider.getProviderName());
                
            } catch (Exception e) {
                logger.error("Error processing rates from {}: {}", 
                    provider.getProviderName(), e.getMessage());
            }
        }
    }

    public List<ExchangeRate> getExchangeRates(String baseCurrency, String targetCurrency) {
        return repository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
    }

    public List<ExchangeRate> getAllRates() {
        return repository.findAll();
    }
}
