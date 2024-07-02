package com.teamsparta.todo.domain.user.service


import com.teamsparta.todo.domain.exception.NotFoundException
import com.teamsparta.todo.domain.user.dto.request.LoginRequest
import com.teamsparta.todo.domain.user.dto.request.SignUpRequest
import com.teamsparta.todo.domain.user.dto.request.UpdateUserProfileRequest
import com.teamsparta.todo.domain.user.dto.response.LoginResponse
import com.teamsparta.todo.domain.user.dto.response.UserResponse
import com.teamsparta.todo.domain.user.exception.InvalidCredentialException
import com.teamsparta.todo.domain.user.model.User
import com.teamsparta.todo.domain.user.model.toResponse
import com.teamsparta.todo.domain.user.repository.UserRepository
import com.teamsparta.todo.infra.security.jwt.JwtPlugin
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
):UserService {

    override fun login(request: LoginRequest, response: HttpServletResponse): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw NotFoundException("user",request.email)

        if (!userRepository.existsByEmail(request.email)){
            throw InvalidCredentialException("이메일 혹은 비밀번호를 확인해주세요")
        }

        val accessToken = jwtPlugin.generateAccessToken(user.id.toString(), user.nickname)

        val cookie = Cookie("accessToken", accessToken)
            .apply {
                path = "/"
                maxAge = 2 * 24 * 60 * 60
                isHttpOnly = true
            }

        response.addCookie(cookie)

        return LoginResponse(accessToken = accessToken)
    }

    @Transactional
    override fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("이미 존재하는 이메일입니다.")
        }
        return userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                nickname = request.nickname
            )
        ).toResponse()
    }

    @Transactional
    override fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException("user", userId)
        user.nickname =  request.nickname
        return userRepository.save(user).toResponse()
    }
}