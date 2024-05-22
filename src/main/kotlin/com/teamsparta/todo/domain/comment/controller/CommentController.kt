package com.teamsparta.todo.domain.comment.controller

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.comment.service.CommentService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos/{todoId}/comments")
class CommentController(
    val commentService: CommentService
) {
    @GetMapping()
    fun getComments(
        @PageableDefault(
            size = 10,
            sort = ["write_date"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable, @PathVariable todoId: Long
    ): ResponseEntity<Page<CommentResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentList(todoId, pageable))
    }

    @PostMapping()
    fun createComment(
        @PathVariable todoId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(todoId, createCommentRequest))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable todoId: Long,
        commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(todoId, commentId, updateCommentRequest))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable todoId: Long, commentId: Long): ResponseEntity<Unit> {
        commentService.deleteComment(todoId, commentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commentService.deleteComment(todoId, commentId))
    }
}