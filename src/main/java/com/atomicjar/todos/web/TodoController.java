package com.atomicjar.todos.web;

import com.atomicjar.todos.entity.TodoEntity;
import com.atomicjar.todos.hn.TodoSyncWithHackerNews;
import com.atomicjar.todos.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
    public Iterable<TodoEntity> getAll() {
        return todoRepository.findAll();
    }

    @PostMapping("/hn")
    public void populateFromHN() {
        todoSyncWithHackerNews.updateTodoWithHackerNewsTopStories(4);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoEntity> getById(@PathVariable String id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<TodoEntity> save(@Valid @RequestBody TodoEntity todo) {
        todo.setId(null);
        TodoEntity savedTodo = todoRepository.save(todo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", savedTodo.getUrl())
                .body(savedTodo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TodoEntity> update(@PathVariable String id, @Valid @RequestBody TodoEntity todo) {
        TodoEntity existingTodo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        if(todo.getCompleted() != null) {
            existingTodo.setCompleted(todo.getCompleted());

            // do something else cool 3rd party service?
        }
        if(todo.getOrder() != null) {
            existingTodo.setOrder(todo.getOrder());
        }
        if(todo.getTitle() != null) {
            existingTodo.setTitle(todo.getTitle());
        }
        TodoEntity updatedTodo = todoRepository.save(existingTodo);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        todoRepository.delete(todo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        todoRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
}
