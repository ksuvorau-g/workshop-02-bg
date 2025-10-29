package com.workshop.exchangerates.scheduler;

import com.workshop.exchangerates.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduler.class);

    private final ExchangeRateService exchangeRateService;

    @Value("${exchange.rates.base.currency:EUR}")
    private String baseCurrency;

    public ExchangeRateScheduler(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    // Run every hour (3600000 ms)
    @Scheduled(fixedRateString = "${exchange.rates.fetch.interval:3600000}")
    public void fetchExchangeRates() {
        logger.info("Starting scheduled exchange rate fetch");
        try {
            exchangeRateService.fetchAndStoreRates(baseCurrency);
            logger.info("Successfully completed scheduled exchange rate fetch");
        } catch (Exception e) {
            logger.error("Error during scheduled exchange rate fetch: {}", e.getMessage(), e);
        }
    }
}
