package com.teamsparta.todo.domain.comment.model

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.todo.model.Todo
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(

    @Column
    var content: String,

    @Column
    var author: String,

    @Column
    val password: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    val todo: Todo

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun isValidAuthor(requestAuthor: String) : Boolean{
        return author == requestAuthor
    }

    fun isValidPassword(requestPassword: String) : Boolean{
        return password == requestPassword
    }


}
fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
        id = id!!,
        content = content,
        author = author
    )
}