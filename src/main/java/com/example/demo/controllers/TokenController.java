package com.example.demo.controllers;

import com.example.demo.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/computeMetadata/v1/instance/service-accounts/default/token")
    public Mono<String> getToken() {
        return tokenService.getToken();
    }

}
