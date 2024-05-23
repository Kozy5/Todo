package com.teamsparta.todo.domain.todo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateTodoRequest(
    @field:Size(min = 1, max = 200, message = "할 일 200자 이내만 작성 가능합니다.")
    val title: String,

    @field:Size(min = 1, max = 1000, message = "할 일 1000자 이내만 작성 가능합니다.")
    val content: String?,

    @field:NotBlank
    val author: String
)
