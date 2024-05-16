package com.teamsparta.todo.domain.todos.dto

data class UpdateTodoRequest(
    val title: String,
    val content: String?,
    val author: String
)
