package com.teamsparta.todo.domain.todo.dto

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import java.time.LocalDateTime

data class TodoWithCommentResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val author: String,
    val status: Boolean,
    val comments: MutableList<CommentResponse>,
    val writeDate: LocalDateTime
)
