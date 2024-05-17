package com.teamsparta.todo.domain.todos.dto

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class CreateTodoRequest(
    val title: String,
    val content: String?,
    val author: String,
    val writeDate:LocalDateTime
)
