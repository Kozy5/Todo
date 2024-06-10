package com.teamsparta.todo.domain.comment.repository

import com.teamsparta.todo.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByTodoIdAndId(todoId: Long, commentId: Long): Comment?
    fun findByTodoId(todoId: Long): List<Comment>
}