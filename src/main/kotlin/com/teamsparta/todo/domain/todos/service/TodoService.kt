package com.teamsparta.todo.domain.todos.service

import com.teamsparta.todo.domain.todos.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todos.dto.TodoResponse
import com.teamsparta.todo.domain.todos.dto.UpdateTodoRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface TodoService {
    fun getAllTodoList(pageable: Pageable): Page<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse

    fun deleteTodo(todoId: Long)
}