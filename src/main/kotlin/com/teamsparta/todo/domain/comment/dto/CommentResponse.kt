package com.teamsparta.todo.domain.comment.dto

data class CommentResponse(
    val id: Long,
    val content: String,
    val author: String
)
