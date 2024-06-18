package com.teamsparta.todo.domain.todo.dto

import com.teamsparta.todo.domain.comment.model.Comment
import java.time.LocalDateTime


data class TodoResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val author: String,
    val status: Boolean,
    val writeDate: LocalDateTime
)
