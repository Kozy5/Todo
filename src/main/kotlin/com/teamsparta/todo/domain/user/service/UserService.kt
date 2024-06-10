package com.teamsparta.todo.domain.user.service


import com.teamsparta.todo.common.exception.NotFoundException
import com.teamsparta.todo.common.status.ROLE
import com.teamsparta.todo.domain.user.dto.request.LoginRequest
import com.teamsparta.todo.domain.user.dto.request.SignUpRequest
import com.teamsparta.todo.domain.user.dto.request.UpdateUserProfileRequest
import com.teamsparta.todo.domain.user.dto.response.LoginResponse
import com.teamsparta.todo.domain.user.dto.response.UserResponse
import com.teamsparta.todo.domain.user.exception.InvalidCredentialException
import com.teamsparta.todo.domain.user.model.User
import com.teamsparta.todo.domain.user.model.UserRole
import com.teamsparta.todo.domain.user.model.toResponse
import com.teamsparta.todo.domain.user.repository.UserRepository
import com.teamsparta.todo.domain.user.repository.UserRoleRepository
import com.teamsparta.todo.infra.security.jwt.JwtPlugin
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw NotFoundException("user",null)

        if (!userRepository.existsByEmail(request.email)){
            throw InvalidCredentialException(request.email)
        }

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.userRole.toString()
            )
        )
    }

    @Transactional
    fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Already exits email : ${request.email}")
        }
        val user = (
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                nickname = request.nickname,
            )
        )
        userRepository.save(user)
        val userRole:UserRole = UserRole(null, ROLE.USER,user)
        userRoleRepository.save(userRole)
        return user.toResponse()
    }

    @Transactional
    fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException("user", userId)
        user.nickname =  request.nickname
        return userRepository.save(user).toResponse()
    }

}