package com.teamsparta.todo.domain.exception

data class NotAuthenticationException(
    val model: String
) : RuntimeException("not-authentication for model: $model")
