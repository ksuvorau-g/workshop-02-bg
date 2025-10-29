package com.workshop.exchangerates.service.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRatesApiProvider implements ExchangeRateProvider {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRatesApiProvider.class);
    private static final String API_URL = "https://api.exchangeratesapi.io/latest";

    private final RestTemplate restTemplate;

    public ExchangeRatesApiProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency) {
        try {
            String url = String.format("%s?base=%s", API_URL, baseCurrency);
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                Map<String, BigDecimal> result = new HashMap<>();
                
                for (Map.Entry<String, Object> entry : rates.entrySet()) {
                    result.put(entry.getKey(), new BigDecimal(entry.getValue().toString()));
                }
                
                return result;
            }
            
            logger.error("Invalid response from ExchangeRatesAPI");
            return new HashMap<>();
            
        } catch (Exception e) {
            logger.error("Error fetching rates from ExchangeRatesAPI: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public String getProviderName() {
        return "exchangeratesapi.io";
    }
}
