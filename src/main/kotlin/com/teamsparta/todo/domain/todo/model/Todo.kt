package com.teamsparta.todo.domain.todo.model

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.comment.model.toResponse
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.TodoWithCommentResponse
import jakarta.persistence.*
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDateTime

@Entity
@Table(name = "todo")
class Todo(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String? = null,

    @Column(name = "author")
    var author: String,

    @Column(name = "status")
    var status: Boolean = false,

    @Column(name = "write_date")
    var writeDate: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "todo", fetch = FetchType.LAZY,cascade = [CascadeType.ALL], orphanRemoval = true,)
    val comments: MutableList<Comment> = mutableListOf()

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Todo.toResponse(): TodoResponse {
    return TodoResponse(
        id = id!!,
        title = title,
        content = content,
        author = author,
        status = status,
        writeDate = writeDate
    )
}

fun Todo.toResponseWithComments():TodoWithCommentResponse{
    val commentsInDto: MutableList<CommentResponse> = mutableListOf()
    for(i in comments){
        commentsInDto += i.toResponse()
    }
    return TodoWithCommentResponse(
        id = id!!,
        title = title,
        content = content,
        author = author,
        status = status,
        comments = commentsInDto,
        writeDate = writeDate
    )
}