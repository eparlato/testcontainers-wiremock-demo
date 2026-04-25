package com.atomicjar.todos.web;

import com.atomicjar.todos.entity.TodoEntity;
import com.atomicjar.todos.repository.SpringTodoRepository;
import com.atomicjar.todos.repository.TodoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TodoControllerTests {
    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    SpringTodoRepository springTodoRepository;

    TodoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TodoRepository(springTodoRepository);
        repository.deleteAll();
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldGetAllTodos() {
        List<TodoEntity> todos = List.of(
                new TodoEntity(null, "Todo Item 1", "aLink", false, 1),
                new TodoEntity(null, "Todo Item 2", "aLink", false, 2)
        );
        repository.saveAll(todos);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    void shouldGetTodoById() {
        TodoEntity todo = repository.save(new TodoEntity(null, "Todo Item 1", "aProperLink", false, 1));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos/{id}", todo.getId())
                .then()
                .statusCode(200)
                .body("title", is("Todo Item 1"))
                .body("completed", is(false))
                .body("link", is("aProperLink"))
                .body("order", is(1));
    }

    @Test
    void shouldCreateTodoSuccessfully() {
        given()
                .contentType(ContentType.JSON)
                .body(
                    """
                    {
                        "title": "Todo Item 1",
                        "completed": false,
                        "order": 1
                    }
                    """
                )
                .when()
                .post("/todos")
                .then()
                .statusCode(201)
                .body("title", is("Todo Item 1"))
                .body("completed", is(false))
                .body("order", is(1));
    }

    @Test
    void shouldDeleteTodoById() {
        TodoEntity todo = repository.save(new TodoEntity(null, "Todo Item 1", "aLink", false, 1));

        assertThat(repository.findById(todo.getId())).isPresent();
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/todos/{id}", todo.getId())
                .then()
                .statusCode(200);

        assertThat(repository.findById(todo.getId())).isEmpty();
    }
}
