package com.teamsparta.todo.common.exception


data class NotFoundException(val modelName:String, val id: Long?):RuntimeException(
    "Model $modelName not found with given id:$id"
)