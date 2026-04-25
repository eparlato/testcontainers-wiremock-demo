package com.atomicjar.todos.domain;

public record Todo(String id, String title, String link, Boolean completed, Integer order) {

}
