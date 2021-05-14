package com.example.demo.services;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class TokenHandler {
     private Mono<String> token;

     private Mono<String> getToken() {
         return this.token;
     }

     private Mono<String> setToken(Mono<String> stringMono) {
         this.token = stringMono;
         return token.cache(Duration.ofMinutes(3));
     }
}
