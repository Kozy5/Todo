package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponse
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.common.exception.InformationDifferentException
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val todoRepository: TodoRepository
) : CommentService {

    override fun createComment(todoId: Long, request: CreateCommentRequest): CommentResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException()
        val comment = Comment(
            content = request.content,
            author = request.author,
            password = request.password,
            todo = todo
        )
        todo.comments.add(comment)
        return commentRepository.save(comment).toResponse()
    }

    override fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw NotFoundException()
        if (!comment.isValidAuthor(request.author) || !comment.isValidPassword(request.password)) throw InformationDifferentException()
        comment.content = request.content
        return commentRepository.save(comment).toResponse()
    }

    override fun deleteComment(todoId: Long, commentId: Long, request: DeleteCommentRequest) {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw NotFoundException()
        if (!comment.isValidAuthor(request.author) || !comment.isValidPassword(request.password)) throw InformationDifferentException()
        commentRepository.delete(comment)
    }
}