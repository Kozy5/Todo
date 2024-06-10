package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.todo.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface TodoService {
    fun getAllTodoList(author: String?, pageable: Pageable): Page<TodoResponse>

    fun getTodoByIdWithComment(todoId: Long): TodoWithCommentResponse

    fun createTodo(request: CreateTodoRequest, userId:Long): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest, userId: Long): TodoResponse

    fun deleteTodo(todoId: Long, userId: Long)

    fun isCompleteTodo(todoId: Long, request: IsCompleteTodoRequest): TodoResponse
}