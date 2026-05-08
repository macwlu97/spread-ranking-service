package com.platform.spreadranking.infrastructure.config;

import com.platform.spreadranking.infrastructure.kanga.KangaApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KangaApiProperties.class)
public class PropertiesConfig {
}