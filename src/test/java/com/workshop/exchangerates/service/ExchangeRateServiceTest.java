package com.workshop.exchangerates.service;

import com.workshop.exchangerates.model.ExchangeRate;
import com.workshop.exchangerates.repository.ExchangeRateRepository;
import com.workshop.exchangerates.service.provider.ExchangeRateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository repository;

    @Mock
    private ExchangeRateProvider provider1;

    @Mock
    private ExchangeRateProvider provider2;

    private ExchangeRateService service;

    @BeforeEach
    void setUp() {
        List<ExchangeRateProvider> providers = Arrays.asList(provider1, provider2);
        service = new ExchangeRateService(repository, providers);
    }

    @Test
    void testFetchAndStoreRates_Success() {
        // Arrange
        String baseCurrency = "EUR";
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("1.10"));
        rates.put("GBP", new BigDecimal("0.85"));

        when(provider1.getProviderName()).thenReturn("TestProvider1");
        when(provider1.getExchangeRates(baseCurrency)).thenReturn(rates);
        when(provider2.getProviderName()).thenReturn("TestProvider2");
        when(provider2.getExchangeRates(baseCurrency)).thenReturn(new HashMap<>());

        // Act
        service.fetchAndStoreRates(baseCurrency);

        // Assert
        verify(repository, times(2)).save(any(ExchangeRate.class));
    }

    @Test
    void testFetchAndStoreRates_EmptyRates() {
        // Arrange
        String baseCurrency = "EUR";
        when(provider1.getProviderName()).thenReturn("TestProvider1");
        when(provider1.getExchangeRates(baseCurrency)).thenReturn(new HashMap<>());
        when(provider2.getProviderName()).thenReturn("TestProvider2");
        when(provider2.getExchangeRates(baseCurrency)).thenReturn(new HashMap<>());

        // Act
        service.fetchAndStoreRates(baseCurrency);

        // Assert
        verify(repository, never()).save(any(ExchangeRate.class));
    }

    @Test
    void testGetExchangeRates() {
        // Arrange
        String baseCurrency = "EUR";
        String targetCurrency = "USD";
        List<ExchangeRate> expectedRates = new ArrayList<>();

        when(repository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency))
                .thenReturn(expectedRates);

        // Act
        List<ExchangeRate> result = service.getExchangeRates(baseCurrency, targetCurrency);

        // Assert
        assertEquals(expectedRates, result);
        verify(repository).findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
    }

    @Test
    void testGetAllRates() {
        // Arrange
        List<ExchangeRate> expectedRates = new ArrayList<>();
        when(repository.findAll()).thenReturn(expectedRates);

        // Act
        List<ExchangeRate> result = service.getAllRates();

        // Assert
        assertEquals(expectedRates, result);
        verify(repository).findAll();
    }
}
