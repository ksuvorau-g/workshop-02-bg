package com.workshop.exchangerates.controller;

import com.workshop.exchangerates.model.ExchangeRate;
import com.workshop.exchangerates.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAllRates() {
        return ResponseEntity.ok(exchangeRateService.getAllRates());
    }

    @GetMapping("/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<List<ExchangeRate>> getExchangeRates(
            @PathVariable String baseCurrency,
            @PathVariable String targetCurrency) {
        List<ExchangeRate> rates = exchangeRateService.getExchangeRates(baseCurrency, targetCurrency);
        return ResponseEntity.ok(rates);
    }

    @PostMapping("/fetch/{baseCurrency}")
    public ResponseEntity<String> fetchRates(@PathVariable String baseCurrency) {
        exchangeRateService.fetchAndStoreRates(baseCurrency);
        return ResponseEntity.ok("Exchange rates fetch initiated for " + baseCurrency);
    }
}
