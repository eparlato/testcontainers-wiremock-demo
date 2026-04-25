package com.atomicjar.todos;

import com.atomicjar.todos.domain.Todo;
import com.atomicjar.todos.repository.TodoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

// run this application with:
// ./mvnw spring-boot:test-run
//
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}

@Component
class DataLoader {

    private final TodoRepository todoRepository;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public DataLoader(TodoRepository todoRepository, JdbcTemplate jdbcTemplate) {
        this.todoRepository = todoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void addTodo() {
        if(!todoRepository.findAll().isEmpty()) {
            return;
        }
        Todo t1 = new Todo(null, "Learn about Testcontainers", null, null, null);
        todoRepository.save(t1);

        Todo t2 = new Todo(null, "Learn about WireMock", null, null, null);
        todoRepository.save(t2);
    }
}
