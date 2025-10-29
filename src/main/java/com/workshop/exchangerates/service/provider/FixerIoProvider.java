package com.workshop.exchangerates.service.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class FixerIoProvider implements ExchangeRateProvider {

    private static final Logger logger = LoggerFactory.getLogger(FixerIoProvider.class);
    private static final String API_URL = "http://data.fixer.io/api/latest";

    @Value("${exchange.rates.fixer.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public FixerIoProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency) {
        try {
            String url = String.format("%s?access_key=%s&base=%s", API_URL, apiKey, baseCurrency);
            
            // Note: Free tier of fixer.io only supports EUR as base currency
            // and requires an API key. For demonstration, we'll return mock data
            // if API key is not configured
            
            if (apiKey == null || apiKey.isEmpty()) {
                logger.warn("Fixer.io API key not configured, returning empty rates");
                return new HashMap<>();
            }
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                Map<String, BigDecimal> result = new HashMap<>();
                
                for (Map.Entry<String, Object> entry : rates.entrySet()) {
                    result.put(entry.getKey(), new BigDecimal(entry.getValue().toString()));
                }
                
                return result;
            }
            
            logger.error("Invalid response from Fixer.io API");
            return new HashMap<>();
            
        } catch (Exception e) {
            logger.error("Error fetching rates from Fixer.io: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public String getProviderName() {
        return "fixer.io";
    }
}
