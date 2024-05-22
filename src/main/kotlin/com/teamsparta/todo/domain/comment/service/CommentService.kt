package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CommentService {

    fun createComment(request: CreateCommentRequest): CommentResponse

    fun updateComment(commentId: Long, request: UpdateCommentRequest): CommentResponse

    fun deleteComment(commentId: Long, request: DeleteCommentRequest)
}