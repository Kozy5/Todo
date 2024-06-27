package com.teamsparta.todo.domain.todo.repository

import com.teamsparta.todo.domain.todo.model.QTodo
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class QueryDslTodoRepository : QueryDslSupport() {

    private val todo = QTodo.todo

    fun searchTodoListByTitle(title: String): List<Todo> {
        return queryFactory.selectFrom(todo)
            .where(todo.title.containsIgnoreCase(title))
            .fetch()
    }
}