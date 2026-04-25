package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.hn.HackernewsItem;

import java.util.List;

public class TodoRepository {

    private final SpringTodoRepository springTodoRepository;

    public TodoRepository(SpringTodoRepository springTodoRepository) {
        this.springTodoRepository = springTodoRepository;
    }

    public void saveHackerNewsItem(HackernewsItem hnItem) {
        String title = hnItem.title();
        List<Todo> byTitle = springTodoRepository.findByTitle(title);
        if (byTitle.isEmpty()) {
            Todo todo = new Todo(null, title, hnItem.url(), false, hnItem.descendants());
            springTodoRepository.save(todo);
        }
    }
}
