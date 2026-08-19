package com.example.busanstamp.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class KakaoMapConfig {

    @Bean
    public RestClient kakaoRestClient(
            RestClient.Builder builder,
            @Value("${kakao.rest-api-key}") String restApiKey
    ) {
        return builder
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "KakaoAK " + restApiKey
                )
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }
}