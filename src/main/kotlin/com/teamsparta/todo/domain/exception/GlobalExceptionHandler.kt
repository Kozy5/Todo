package com.teamsparta.todo.domain.exception

import com.teamsparta.todo.domain.exception.dto.ErrorResponse
import com.teamsparta.todo.domain.user.exception.InvalidCredentialException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun handleModelNotFoundException(e: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(message = e.message, "404"))
    }

    @ExceptionHandler(NotAuthenticationException::class)
    fun handleNotAuthenticatedException(e: NotAuthenticationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(message = e.message,"401"))
    }


    @ExceptionHandler(InvalidCredentialException::class)
    fun handleInvalidCredentialException(e: InvalidCredentialException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message, "400"))
    }
}