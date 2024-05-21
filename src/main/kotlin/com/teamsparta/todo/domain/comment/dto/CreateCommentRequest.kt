package com.teamsparta.todo.domain.comment.dto

data class CreateCommentRequest(
    val content: String,
    val author: String,
    val password: String
)
