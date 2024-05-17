package com.teamsparta.todo.domain.todos.dto

import java.time.LocalDateTime


data class TodoResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val author: String,
    val writeDate: LocalDateTime
)
