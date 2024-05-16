package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl:TodoService {
    override fun getAllTodoList(): List<TodoResponse> {
        TODO("Not yet implemented")
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        TODO("Not yet implemented")
    }

    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        TODO("Not yet implemented")
    }

    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        TODO("Not yet implemented")
    }

    override fun deleteTodo(todoId: Long) {
        TODO("Not yet implemented")
    }
}