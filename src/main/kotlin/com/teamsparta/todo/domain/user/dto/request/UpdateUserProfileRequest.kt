package com.teamsparta.todo.domain.user.dto.request

data class UpdateUserProfileRequest(
    var email: String,
    val nickname:String
)
