package com.atomicjar.todos.hn;

import com.atomicjar.todos.repository.TodoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class TodoSyncWithHackerNews {

  private final String baseUrl;
    private final TodoRepository todoRepository;

    public TodoSyncWithHackerNews(String baseUrl, TodoRepository todoRepository) {
        this.baseUrl = baseUrl;
        this.todoRepository = todoRepository;
    }


  // method to return Spring WebClient object for querying the Hackernews API
  public WebClient getWebClient() {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  public void updateTodoWithHackerNewsTopStories(int n) {
    Mono.fromRunnable(() -> {
      List<Integer> ids = fetchTopStoryIds(n);
      for (Integer id : ids) {
        HackernewsItem item = fetchItem(id);
        todoRepository.saveHackerNewsItem(item);
      }
    }).subscribeOn(Schedulers.boundedElastic()).subscribe();
  }

  List<Integer> fetchTopStoryIds(int n) {
    return getWebClient().get()
        .uri("beststories.json")
        .retrieve()
        .bodyToFlux(Integer.class)
        .take(n)
        .collectList()
        .block();
  }

  HackernewsItem fetchItem(Integer id) {
    return getWebClient().get()
        .uri("item/{id}.json", id)
        .retrieve()
        .bodyToMono(HackernewsItem.class)
        .block();
  }

}
