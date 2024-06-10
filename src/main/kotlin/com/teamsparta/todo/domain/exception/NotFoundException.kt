package com.teamsparta.todo.domain.exception


data class NotFoundException(val modelName:String, val id: Long?):RuntimeException(
    "Model $modelName not found with given id:$id"
)