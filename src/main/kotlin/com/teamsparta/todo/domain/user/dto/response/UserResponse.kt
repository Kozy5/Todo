package com.teamsparta.todo.domain.user.dto.response

import com.teamsparta.todo.domain.user.model.UserRole

data class UserResponse(
    val id:Long,
    val email:String,
    val nickname:String,
    val role:String
)
