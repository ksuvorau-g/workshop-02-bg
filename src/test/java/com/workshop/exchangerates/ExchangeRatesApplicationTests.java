package com.workshop.exchangerates;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.liquibase.enabled=false"
})
class ExchangeRatesApplicationTests {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }
}
