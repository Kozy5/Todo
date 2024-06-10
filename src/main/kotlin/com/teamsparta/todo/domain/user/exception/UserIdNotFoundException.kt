package com.teamsparta.todo.domain.user.exception

data class UserIdNotFoundException(val userId: String) : RuntimeException("Not Found UserId: $userId")
