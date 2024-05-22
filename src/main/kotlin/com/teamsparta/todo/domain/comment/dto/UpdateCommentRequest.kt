package com.teamsparta.todo.domain.comment.dto

data class UpdateCommentRequest(
    val content: String,
    val author: String,
    val password: String
)
