package com.atomicjar.todos.config;

import com.atomicjar.todos.hn.TodoSyncWithHackerNews;
import com.atomicjar.todos.repository.SpringTodoRepository;
import com.atomicjar.todos.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    TodoRepository todoRepository(SpringTodoRepository springTodoRepository) {
        return new TodoRepository(springTodoRepository);
    }

    @Bean
    TodoSyncWithHackerNews todoSyncWithHackerNews(
        @Value("${hackernews.base-url:https://hacker-news.firebaseio.com/v0/}") String baseUrl,
        TodoRepository todoRepository) {
        return new TodoSyncWithHackerNews(baseUrl, todoRepository);
    }
}
