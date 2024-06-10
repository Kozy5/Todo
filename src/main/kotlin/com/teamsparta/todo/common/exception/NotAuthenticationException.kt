package com.teamsparta.todo.common.exception

data class NotAuthenticationException(
    val model: String
) : RuntimeException("not-authentication for model: $model")
