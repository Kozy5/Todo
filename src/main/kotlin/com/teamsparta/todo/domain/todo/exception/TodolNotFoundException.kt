package com.teamsparta.todo.domain.todo.exception


data class TodoNotFoundException(val todoId: Long):RuntimeException(
    "  not fount with given id: $todoId"
)