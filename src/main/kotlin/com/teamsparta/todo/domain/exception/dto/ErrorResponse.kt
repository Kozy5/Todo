package com.teamsparta.todo.domain.exception.dto

data class ErrorResponse(
    val message: String?,
    val errorCode: String
)