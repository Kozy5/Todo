package com.teamsparta.todo.domain.comment.repository

import com.teamsparta.todo.domain.comment.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByTodoId(todoId: Long): Page<Comment>

    fun findByTodoIdAndId(todoId: Long, commentId: Long): Comment?

    fun deleteAllByTodoId(todoId: Long)
}