package com.workshop.exchangerates.service.provider;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRateProvider {
    
    /**
     * Get exchange rates from the provider
     * @param baseCurrency Base currency code (e.g., EUR)
     * @return Map of currency codes to exchange rates
     */
    Map<String, BigDecimal> getExchangeRates(String baseCurrency);
    
    /**
     * Get the provider name
     * @return Provider name
     */
    String getProviderName();
}
