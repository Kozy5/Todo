package com.teamsparta.todo.domain.todo.repository

import com.teamsparta.todo.domain.todo.model.Todo

interface CustomTodoRepository {

    fun searchTodoListByTitle(title: String): List<Todo>
}