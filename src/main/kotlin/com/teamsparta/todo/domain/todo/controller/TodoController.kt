package com.teamsparta.todo.domain.todo.controller


import com.teamsparta.todo.domain.todo.dto.*
import com.teamsparta.todo.domain.todo.service.TodoService
import com.teamsparta.todo.infra.security.jwt.UserPrincipal
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RequestMapping("/todos")
@RestController
class TodoController(
    val todoService: TodoService
) {

    @GetMapping("/search")
    fun searchTodoList(
        @PageableDefault(size = 12, sort = ["writeDate"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam title: String?,
        @RequestParam nickname: String?,
        @RequestParam status: Boolean?,
        @RequestParam daysAgo: Long?
    ): ResponseEntity<Page<TodoResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.searchTodoList(pageable, title, nickname, status, daysAgo))
    }


    @GetMapping
    fun getTodos(
        @ParameterObject
        @PageableDefault(
            size = 10,
            sort = ["writeDate"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @RequestParam(value = "status", required = false) status: Boolean?
    ): ResponseEntity<Page<TodoResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodoList(pageable, status))
    }


    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoWithCommentResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoByIdWithComment(todoId))
    }


    @PostMapping
    fun createTodo(@Valid @RequestBody createTodoRequest: CreateTodoRequest): ResponseEntity<TodoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(createTodoRequest, userId))
    }


    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @Valid @RequestBody updateTodoRequest: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, updateTodoRequest, userId))
    }


    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable todoId: Long): ResponseEntity<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
        todoService.deleteTodo(todoId, userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


    @PatchMapping("/{todoId}")
    fun isCompleteTodo(
        @PathVariable todoId: Long,
        @RequestBody isCompleteTodoRequest: IsCompleteTodoRequest
    ): ResponseEntity<TodoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
        return ResponseEntity.status(HttpStatus.OK)
            .body(todoService.isCompleteTodo(todoId, isCompleteTodoRequest, userId))
    }
}