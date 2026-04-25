package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.hn.HackernewsItem;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

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

    public List<Todo> findAll() {
        return springTodoRepository.findAll();
    }

    public Optional<Todo> findById(String id) {
        return springTodoRepository.findById(id);
    }

    public Todo save(@Valid Todo todo) {
        return springTodoRepository.save(todo);
    }

    public void delete(Todo todo) {
        springTodoRepository.delete(todo);
    }

    public void deleteAll() {
        springTodoRepository.deleteAll();
    }

    public void saveAll(List<Todo> todos) {
        springTodoRepository.saveAll(todos);
    }

    public List<Todo> getPendingTodos() {
        return springTodoRepository.getPendingTodos();
    }
}
