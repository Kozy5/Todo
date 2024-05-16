package com.teamsparta.todo.domain.todo.dto

import java.util.Date

data class TodoResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val author: String,
    val writeDate:Date
)
