package com.teamsparta.todo.domain.todo.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e:ModelNotFoundException): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .build()
    }
}