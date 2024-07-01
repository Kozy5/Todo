package com.teamsparta.todo.domain.exception


data class NotFoundException(val modelName:String, val id: Any):RuntimeException(
    "Model $modelName not found with given id:$id"
)