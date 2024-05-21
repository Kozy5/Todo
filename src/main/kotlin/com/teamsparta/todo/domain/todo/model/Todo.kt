package com.teamsparta.todo.domain.todo.model

import com.teamsparta.todo.domain.todo.dto.TodoResponse
import jakarta.persistence.*
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDateTime

@Entity
@Table(name = "todo")
class Todo(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String?,


    @Column(name = "author")
    var author: String,

    @Column(name = "status")
    var status: Boolean = false,

    @Column(name = "write_date")
    var writeDate: LocalDateTime = LocalDateTime.now(),

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

