package com.teamsparta.todo.domain.user.exception

data class UserIdIllegalStateException(val userId: String) : RuntimeException("already use in userId:$userId")