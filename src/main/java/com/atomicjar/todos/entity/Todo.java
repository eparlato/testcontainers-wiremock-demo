package com.atomicjar.todos.entity;

public record Todo(String id, String title, String link, Boolean completed, Integer order) {

}
