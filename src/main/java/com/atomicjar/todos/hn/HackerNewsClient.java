package com.atomicjar.todos.hn;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class HackerNewsClient {
    private final WebClient webClient;

    public HackerNewsClient(String baseUrl) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    List<Integer> fetchTopStoryIds(int n) {
        return webClient.get()
            .uri("beststories.json")
            .retrieve()
            .bodyToFlux(Integer.class)
            .take(n)
            .collectList()
            .block();
    }

    HackernewsItem fetchItem(Integer id) {
        return webClient.get()
            .uri("item/{id}.json", id)
            .retrieve()
            .bodyToMono(HackernewsItem.class)
            .block();
    }
}
