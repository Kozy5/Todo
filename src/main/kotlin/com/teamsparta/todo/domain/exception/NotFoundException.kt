package com.teamsparta.todo.domain.exception


data class NotFoundException(val id: Long):RuntimeException(
    "  not fount with given id: $id"
)