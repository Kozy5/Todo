package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.IsCompleteTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface TodoService {
    fun getAllTodoList(pageable: Pageable): Page<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse

    fun deleteTodo(todoId: Long)

    fun isCompleteTodo(todoId: Long,request: IsCompleteTodoRequest):TodoResponse
}