package com.teamsparta.todo.domain.user.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @field:NotBlank(message = "Email address must not be blank")
    @field:Email(message = "Email address should be format-valid")
    val email:String,

    @field:NotBlank(message = "Password should not be blank")
    @field:Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,15}$",
        message = "Password should be at least 8 letters long and at most 15 characters long, including at least ONE uppercase, lowercase, and special letters."
    )
    val password:String,

    @field:NotBlank(message = "Username must not be blank")
    @field:Pattern(
        regexp = "^[a-z0-9]{4,10}$",
        message = "Username should be [4, 10] letters long, only with lowercase letters and numbers."
    )
    val nickname:String,
)
