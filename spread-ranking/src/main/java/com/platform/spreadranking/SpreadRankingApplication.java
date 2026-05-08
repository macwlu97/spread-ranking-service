package com.platform.spreadranking;

import com.platform.spreadranking.api.security.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityProperties.class)
public class SpreadRankingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpreadRankingApplication.class, args);
    }
}