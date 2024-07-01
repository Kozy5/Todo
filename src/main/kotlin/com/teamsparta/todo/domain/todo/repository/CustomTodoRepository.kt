package com.teamsparta.todo.domain.todo.repository

import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.model.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomTodoRepository {

    fun searchTodoList(
        pageable: Pageable,
        title: String?,
        author: String?,
        status: Boolean?,
        daysAgo: Long?
    ): Page<Todo>

    fun findByPageableAndStatus(pageable: Pageable, status: Boolean?): Page<Todo>
}