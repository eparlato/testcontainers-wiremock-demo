package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.TodoEntity;
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
        List<TodoEntity> byTitle = springTodoRepository.findByTitle(title);
        if (byTitle.isEmpty()) {
            TodoEntity todo = new TodoEntity(null, title, hnItem.url(), false, hnItem.descendants());
            springTodoRepository.save(todo);
        }
    }

    public List<TodoEntity> findAll() {
        return springTodoRepository.findAll();
    }

    public Optional<TodoEntity> findById(String id) {
        return springTodoRepository.findById(id);
    }

    public TodoEntity save(@Valid TodoEntity todo) {
        return springTodoRepository.save(todo);
    }

    public void delete(TodoEntity todo) {
        springTodoRepository.delete(todo);
    }

    public void deleteAll() {
        springTodoRepository.deleteAll();
    }

    public void saveAll(List<TodoEntity> todos) {
        springTodoRepository.saveAll(todos);
    }

    public List<TodoEntity> getPendingTodos() {
        return springTodoRepository.getPendingTodos();
    }
}
