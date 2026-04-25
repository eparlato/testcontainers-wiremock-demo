package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.TodoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface SpringTodoRepository extends ListCrudRepository<TodoEntity, String>, ListPagingAndSortingRepository<TodoEntity, String> {
    @Query("select t from TodoEntity t where t.completed = false")
    List<TodoEntity> getPendingTodos();

    List<TodoEntity> findByTitle(String title);
}
