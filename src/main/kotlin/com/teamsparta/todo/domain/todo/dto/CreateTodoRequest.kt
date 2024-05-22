package com.teamsparta.todo.domain.todo.dto

import java.time.LocalDateTime

data class CreateTodoRequest(
    val title: String,
    val content: String?,
    val author: String
)
