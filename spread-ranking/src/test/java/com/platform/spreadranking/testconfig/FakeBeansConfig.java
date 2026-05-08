package com.platform.spreadranking.testconfig;

import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.testconfig.fake.FakeOrderBookProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class FakeBeansConfig {

    @Bean
    @Primary
    public OrderBookProviderPort orderBookProviderPort() {
        return new FakeOrderBookProvider();
    }
}