package com.teamsparta.todo.domain.user.dto.request

data class SignUpRequest(
    val email:String,
    val password:String,
    val nickname:String,
    val role:String
)
