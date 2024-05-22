package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponse
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.PasswordDifferentException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository
) : CommentService {

    override fun createComment(request: CreateCommentRequest): CommentResponse {
        TODO()
    }

    override fun updateComment(commentId: Long, request: UpdateCommentRequest): CommentResponse {
        TODO()
    }

    override fun deleteComment(commentId: Long, request: DeleteCommentRequest) {
        TODO("Not yet implemented")
    }
}