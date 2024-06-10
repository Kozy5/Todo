package com.teamsparta.todo.domain.user.dto.request

data class UpdateUserProfileRequest(
    var id: Long?,
    val nickname:String
)
