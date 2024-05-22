package com.teamsparta.todo.domain.comment.dto

data class DeleteCommentRequest(
    val author: String,
    val password: String
)
