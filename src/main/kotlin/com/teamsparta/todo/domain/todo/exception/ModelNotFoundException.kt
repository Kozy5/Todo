package com.teamsparta.todo.domain.todo.exception

data class ModelNotFoundException(val modelName: String, val id: Long):RuntimeException(
    "Model ${modelName} not fount with given id: $id"
)