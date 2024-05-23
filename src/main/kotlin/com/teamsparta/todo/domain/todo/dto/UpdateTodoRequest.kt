package com.teamsparta.todo.domain.todo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateTodoRequest(
    @field:Size(min = 1, max = 200)
    val title: String,

    @field:Size(min = 1, max = 1000)
    val content: String?,

    @field:NotBlank
    val author: String
)
