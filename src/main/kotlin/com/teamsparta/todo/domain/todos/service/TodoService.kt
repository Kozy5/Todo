package com.teamsparta.todo.domain.todos.service

import com.teamsparta.todo.domain.todos.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todos.dto.TodoResponse
import com.teamsparta.todo.domain.todos.dto.UpdateTodoRequest


interface TodoService {
    fun getAllTodoList(): List<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse

    fun deleteTodo(todoId: Long)
}