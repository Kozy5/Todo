package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CommentService {

    fun createComment(todoId:Long,request: CreateCommentRequest): CommentResponse

    fun updateComment(todoId:Long,commentId: Long, request: UpdateCommentRequest): CommentResponse

    fun deleteComment(todoId:Long,commentId: Long, request: DeleteCommentRequest)
}