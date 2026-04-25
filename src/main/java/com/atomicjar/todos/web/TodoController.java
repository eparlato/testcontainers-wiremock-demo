package com.atomicjar.todos.web;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.hn.TodoSyncWithHackerNews;
import com.atomicjar.todos.repository.TodoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoRepository todoRepository;
    private final TodoSyncWithHackerNews todoSyncWithHackerNews;

    public TodoController(TodoRepository todoRepository, TodoSyncWithHackerNews todoSyncWithHackerNews) {
        this.todoRepository = todoRepository;
        this.todoSyncWithHackerNews = todoSyncWithHackerNews;
    }

    @GetMapping
    public Iterable<Todo> getAll() {
        return todoRepository.findAll();
    }

    @PostMapping("/hn")
    public void populateFromHN() {
        todoSyncWithHackerNews.updateTodoWithHackerNewsTopStories(4);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable String id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<Todo> save(@Valid @RequestBody Todo todo) {
        Todo savedTodo = todoRepository.save(nullifyIncomingTodoId(todo));
        String locationUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/todos/" + savedTodo.id();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", locationUrl)
                .body(savedTodo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable String id, @Valid @RequestBody Todo todo) {
        Todo existingTodo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        Todo merged = new Todo(
                existingTodo.id(),
                todo.title() != null ? todo.title() : existingTodo.title(),
                existingTodo.link(),
                todo.completed() != null ? todo.completed() : existingTodo.completed(),
                todo.order() != null ? todo.order() : existingTodo.order()
        );
        System.out.printf("updated from [%s] top [%s]%n", existingTodo, merged);
        Todo updatedTodo = todoRepository.save(merged);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        todoRepository.delete(todo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        todoRepository.deleteAll();
        return ResponseEntity.ok().build();
    }


    private Todo nullifyIncomingTodoId(Todo todo) {
        return new Todo(null, todo.title(), todo.link(), todo.completed(), todo.order());
    }
}
