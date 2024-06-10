package com.teamsparta.todo.domain.comment.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponse
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.NotAuthenticationException
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import com.teamsparta.todo.domain.user.repository.UserRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) : CommentService {

    override fun createComment(todoId: Long, request: CreateCommentRequest, userId: Long): CommentResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException()
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException()
        val comment = Comment(
            content = request.content,
            author = request.author,
            todo = todo,
            user = user
        )
        return commentRepository.save(comment).toResponse()
    }

    override fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest,userId:Long): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw NotFoundException()
        if (userId != comment.user.id) throw NotAuthenticationException("comment")
        comment.content = request.content
        return commentRepository.save(comment).toResponse()
    }

    override fun deleteComment(todoId: Long, commentId: Long, request: DeleteCommentRequest,userId:Long) {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw NotFoundException()
        if (userId != comment.todo.user?.id) throw NotAuthenticationException("comment")
        commentRepository.delete(comment)
    }
}