package com.example.demo.controllers;

import com.example.demo.services.MainService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/srv/o")
@AllArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/{name}/{version}/{anySubPath}")
    public Mono<ResponseEntity<Mono<?>>> getInfo(@PathVariable("name") String name,
                                                 @PathVariable("version") String version,
                                                 @PathVariable("anySubPath") String anySubPath) {
        return mainService.getMediaIfNoneMatch(name, version, anySubPath);
    }

    @GetMapping("/{name}/index.html")
    public Mono<ResponseEntity<Mono<String>>> getInfo(@PathVariable("name") String name) {
        return mainService.getMedia(name);
    }
}
