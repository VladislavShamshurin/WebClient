package com.example.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class TokenService {
    private Mono<String> cacheToken;

    public Mono<String> getToken() {
        cacheToken =
                WebClient
                .builder()
                .build()
                .get()
                .header("Metadata-Flavor", "Google")
                .retrieve()
                .bodyToMono(String.class)
                .cache(Duration.ofMinutes(3));
        return cacheToken;
    }

    public Mono<String> getCacheToken() {
        return cacheToken;
    }

    public void setCacheToken(Mono<String> cacheToken) {
        this.cacheToken = cacheToken;
    }
}
