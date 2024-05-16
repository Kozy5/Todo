package com.teamsparta.todo.domain.todo.dto

data class CreateTodoRequest(
    val title: String,
    val content: String?,
    val author: String
)
