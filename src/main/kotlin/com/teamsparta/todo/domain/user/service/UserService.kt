package com.teamsparta.todo.domain.user.service

import com.teamsparta.todo.domain.user.dto.request.LoginRequest
import com.teamsparta.todo.domain.user.dto.request.SignUpRequest
import com.teamsparta.todo.domain.user.dto.request.UpdateUserProfileRequest
import com.teamsparta.todo.domain.user.dto.response.LoginResponse
import com.teamsparta.todo.domain.user.dto.response.UserResponse
import jakarta.servlet.http.HttpServletResponse

interface UserService {
    fun signUp(request: SignUpRequest): UserResponse
    fun login(request: LoginRequest, response: HttpServletResponse): LoginResponse
    fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse
}