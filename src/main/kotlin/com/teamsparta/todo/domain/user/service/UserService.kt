package com.teamsparta.todo.domain.user.service


import com.teamsparta.todo.common.exception.NotFoundException
import com.teamsparta.todo.domain.user.dto.request.LoginRequest
import com.teamsparta.todo.domain.user.dto.request.SignUpRequest
import com.teamsparta.todo.domain.user.dto.request.UpdateUserProfileRequest
import com.teamsparta.todo.domain.user.dto.response.LoginResponse
import com.teamsparta.todo.domain.user.dto.response.UserResponse
import com.teamsparta.todo.domain.user.exception.InvalidCredentialException
import com.teamsparta.todo.domain.user.model.User
import com.teamsparta.todo.common.UserRole
import com.teamsparta.todo.domain.user.model.toResponse
import com.teamsparta.todo.domain.user.repository.UserRepository
import com.teamsparta.todo.infra.security.jwt.JwtPlugin
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw NotFoundException("user",null)

        if (user.role.name != request.role || !passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialException()
        }

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.role.name
            )
        )
    }

    @Transactional
    fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Already exits email : ${request.email}")
        }
        return userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                nickname = request.nickname,
                role = when (request.role) {
                    UserRole.USER.name -> UserRole.USER
                    else -> throw IllegalStateException("Invalid role : $request.role")
                }
            )
        ).toResponse()
    }

    @Transactional
    fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException("user", userId)
        user.nickname =  request.nickname
        return userRepository.save(user).toResponse()
    }

}