package com.example.demo.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MainService {
    private final TokenService tokenService;

    public Mono<ResponseEntity<Mono<?>>> getMediaIfNoneMatch(String name, String version, String anySubPath) {
        return tokenService.getCacheToken().flatMap(str -> {
            JsonNode jsonNode = new TextNode(str);
            return WebClient
                    .builder()
                    .build()
                    .get()
                    .uri(String.format("/srv/o/%s/%s/%s", name, version, anySubPath))
                    .header("Authorization", String.format("Bearer %s", jsonNode.get("access_token").toString()))
                    .exchange()
                    .map(clientResponse -> {
                        HttpHeaders httpHeaders = clientResponse.headers().asHttpHeaders();
                        if (httpHeaders.containsKey("if-none-match")) {
                            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
                        }
                        httpHeaders.add("cache-control", "public, max-age=31536000");
                        httpHeaders.add("ETag", String.valueOf(System.currentTimeMillis()));
                        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                        httpHeaders.toSingleValueMap().forEach(headers::add);
                        return new ResponseEntity<>(clientResponse.bodyToMono(String.class), headers,
                                1);
                    });
        });
    }

    public Mono<ResponseEntity<Mono<String>>> getMedia(String name) {
        return tokenService.getCacheToken().flatMap(str -> {
            JsonNode jsonNode = new TextNode(str);
            return WebClient
                .builder()
                .build()
                .get()
                .uri(String.format("/srv/o/%s/index.html", name))
                .header("Authorization", String.format("Bearer %s", jsonNode.get("access_token").toString()))
                .exchange()
                .map(clientResponse -> {
                    clientResponse.headers().asHttpHeaders().add("cache-control",
                            "no-cache, no-store, must-revalidate");
                    return new ResponseEntity<>(clientResponse.bodyToMono(String.class),
                            HttpStatus.OK);
                });
        });
    }
}
