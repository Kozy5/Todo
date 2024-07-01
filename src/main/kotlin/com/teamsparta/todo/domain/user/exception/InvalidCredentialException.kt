package com.teamsparta.todo.domain.user.exception

data class InvalidCredentialException(
    val phrase: String
):RuntimeException("Invalid Credential: $phrase")