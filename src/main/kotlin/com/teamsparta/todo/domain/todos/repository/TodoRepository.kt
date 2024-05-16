package com.teamsparta.todo.domain.todos.repository

import com.teamsparta.todo.domain.todos.model.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long> {

}