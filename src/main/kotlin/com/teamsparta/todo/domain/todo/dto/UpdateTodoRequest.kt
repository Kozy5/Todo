package com.teamsparta.todo.domain.todo.dto

data class UpdateTodoRequest(
    val title: String,
    val content: String?,
    val author: String
)
