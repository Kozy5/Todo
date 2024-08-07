package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.todo.dto.*
import com.teamsparta.todo.domain.todo.model.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface TodoService {
    fun getAllTodoList(pageable: Pageable, status: Boolean?): Page<TodoResponse>

    fun getTodoByIdWithComment(todoId: Long): TodoWithCommentResponse

    fun createTodoWithLock(request: CreateTodoRequest, userId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest, userId: Long): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest, userId: Long): TodoResponse

    fun deleteTodo(todoId: Long, userId: Long)

    fun isCompleteTodo(todoId: Long, request: IsCompleteTodoRequest, userId: Long): TodoResponse

    fun searchTodoList(
        pageable: Pageable,
        title: String?,
        nickname: String?,
        status: Boolean?,
        daysAgo: Long?
    ): Page<TodoResponse>?

}