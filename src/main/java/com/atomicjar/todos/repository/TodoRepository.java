package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.entity.TodoEntity;
import com.atomicjar.todos.hn.HackernewsItem;

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

    public List<Todo> findAll() {
        List<TodoEntity> allTodosFromDb = springTodoRepository.findAll();
        return allTodosFromDb.stream().map(TodoEntity::toTodo).toList();
    }

    public Optional<Todo> findById(String id) {
        return springTodoRepository.findById(id).map(TodoEntity::toTodo);
    }

    public Todo save(Todo todo) {
        TodoEntity entity = TodoEntity.fromTodo(todo);
        return springTodoRepository.save(entity).toTodo();
    }

    public void delete(Todo todo) {
        springTodoRepository.delete(TodoEntity.fromTodo(todo));
    }

    public void deleteAll() {
        springTodoRepository.deleteAll();
    }

    public void saveAll(List<Todo> todos) {
        springTodoRepository.saveAll(todos.stream().map(TodoEntity::fromTodo).toList());
    }

    public List<Todo> getPendingTodos() {
        return springTodoRepository.getPendingTodos().stream().map(TodoEntity::toTodo).toList();
    }
}
