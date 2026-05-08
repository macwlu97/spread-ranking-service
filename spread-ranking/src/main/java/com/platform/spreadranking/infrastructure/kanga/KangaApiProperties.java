package com.platform.spreadranking.infrastructure.kanga;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kanga.api")
public record KangaApiProperties(
        String baseUrl
) {
}