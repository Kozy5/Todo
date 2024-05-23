package com.teamsparta.todo.domain.todo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateTodoRequest(
    @field:Size(min = 1, max = 200, message = "할 일 제목은 200자 이내만 작성 가능합니다.")
    val title: String,

    @field:Size(min = 1, max = 1000, message = "할 일 내용은 1000자 이내만 작성 가능합니다.")
    val content: String?,

    @field:NotBlank
    val author: String
)
