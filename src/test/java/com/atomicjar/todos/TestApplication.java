package com.atomicjar.todos;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.repository.SpringTodoRepository;
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

    private SpringTodoRepository springTodoRepository;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public DataLoader(SpringTodoRepository springTodoRepository, JdbcTemplate jdbcTemplate) {
        this.springTodoRepository = springTodoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void addTodo() {
        if(!springTodoRepository.findAll().isEmpty()) {
            return;
        }
        Todo t1 = new Todo();
        t1.setTitle("Learn about Testcontainers");
        springTodoRepository.save(t1);

        Todo t2 = new Todo();
        t2.setTitle("Learn about WireMock");
        springTodoRepository.save(t2);
    }
}
