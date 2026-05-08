package com.platform.spreadranking.infrastructure.kanga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient kangaRestClient(KangaApiProperties props) {
        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .build();
    }
}