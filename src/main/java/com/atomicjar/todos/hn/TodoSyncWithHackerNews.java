package com.atomicjar.todos.hn;

import com.atomicjar.todos.repository.TodoRepository;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class TodoSyncWithHackerNews {

    private final TodoRepository todoRepository;
    private final HackerNewsClient hackerNewsClient;

    public TodoSyncWithHackerNews(HackerNewsClient hackerNewsClient, TodoRepository todoRepository) {
        this.hackerNewsClient = hackerNewsClient;
        this.todoRepository = todoRepository;
    }

  public void updateTodoWithHackerNewsTopStories(int n) {
    Mono.fromRunnable(() -> {
      List<Integer> ids = hackerNewsClient.fetchTopStoryIds(n);
      for (Integer id : ids) {
        HackernewsItem item = hackerNewsClient.fetchItem(id);
        todoRepository.saveHackerNewsItem(item);
      }
    }).subscribeOn(Schedulers.boundedElastic()).subscribe();
  }
}
