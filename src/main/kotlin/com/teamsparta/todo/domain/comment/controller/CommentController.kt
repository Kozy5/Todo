package com.teamsparta.todo.domain.comment.controller

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos/{todoId}/comments")
class CommentController {
    @GetMapping()
    fun getComents(): ResponseEntity<List<CommentResponse>> {
        TODO()
    }

    @PostMapping()
    fun createComment(@RequestBody createCommentRequest: CreateCommentRequest): ResponseEntity<CommentResponse> {
        TODO()
    }

    @PutMapping("/{commentId}")
    fun updateComment(@PathVariable commentId: Long, @RequestBody updateCommentRequest: UpdateCommentRequest): ResponseEntity<CommentResponse> {
        TODO()
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long):ResponseEntity<Unit>{
        TODO()
    }
}