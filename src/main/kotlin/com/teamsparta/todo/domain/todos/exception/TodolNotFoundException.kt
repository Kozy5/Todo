package com.teamsparta.todo.domain.todos.exception


data class TodoNotFoundException(val todoId: Long):RuntimeException(
    "  not fount with given id: $todoId"
)