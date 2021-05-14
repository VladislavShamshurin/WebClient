package com.example.demo;

import com.example.demo.controllers.MainController;
import com.example.demo.controllers.TokenController;
import com.example.demo.services.MainService;
import com.example.demo.services.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {MainController.class, TokenController.class})
@Import({MainService.class, TokenService.class})
class Demo2ApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MainService mainService;

    @MockBean
    private TokenService tokenService;

    @Test
    void contextLoads() {
        Mockito.when(tokenService.getCacheToken()).thenReturn(Mono.just("2adwF31aAD4a4awffFv21Md"));

        webTestClient
                .get()
                .uri("/srv/o/{name}/index.html", "test")
                .exchange()
                .expectBody()
                .consumeWith(result -> Assertions.assertThat(result.getResponseBody()).isNotEmpty());
    }
}
