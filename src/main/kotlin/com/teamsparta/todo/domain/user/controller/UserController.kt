package com.teamsparta.todo.domain.user.controller



import com.teamsparta.todo.common.dto.CustomUser
import com.teamsparta.todo.domain.user.dto.request.LoginRequest
import com.teamsparta.todo.domain.user.dto.request.SignUpRequest
import com.teamsparta.todo.domain.user.dto.request.UpdateUserProfileRequest
import com.teamsparta.todo.domain.user.dto.response.LoginResponse
import com.teamsparta.todo.domain.user.dto.response.UserResponse
import com.teamsparta.todo.domain.user.service.UserService
import jakarta.annotation.security.PermitAll
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest):ResponseEntity<LoginResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(request))
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.signUp(signUpRequest))
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/info")
    fun updateUserProfile(@RequestBody updateUserProfileRequest: UpdateUserProfileRequest):ResponseEntity<UserResponse>{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        updateUserProfileRequest.id = userId
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updateUserProfile(userId,updateUserProfileRequest))
    }
}