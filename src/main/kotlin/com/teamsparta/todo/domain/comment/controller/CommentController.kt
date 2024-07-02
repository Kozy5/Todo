package com.teamsparta.todo.domain.comment.controller


import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.DeleteCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.comment.service.CommentService
import com.teamsparta.todo.infra.security.jwt.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos/{todoId}/comments")
class CommentController(
    val commentService: CommentService
) {

    @PostMapping()
    fun createComment(
        @PathVariable todoId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(todoId, createCommentRequest, userId))
    }


    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable todoId: Long,@PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(todoId, commentId, updateCommentRequest,userId))
    }


    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        deleteCommentRequest: DeleteCommentRequest
    ): ResponseEntity<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as UserPrincipal).id
        commentService.deleteComment(todoId, commentId, deleteCommentRequest,userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}