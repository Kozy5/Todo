package com.teamsparta.todo.domain.todos.model

import com.teamsparta.todo.domain.todos.dto.TodoResponse
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Entity
@Table(name = "todo")
class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "title")
    var title: String = ""

    @Column(name = "content")
    var content: String? = null

    @CreatedDate
    @Column(name = "write_date")
    var writeDate: Date? = null

    @Column(name = "author")
    var author: String = ""


    constructor()

    constructor(title: String, content: String?, writeDate: Date?, author: String) {
        this.title = title
        this.content = content
        this.writeDate = writeDate
        this.author = author
    }
}
fun Todo.toResponse(): TodoResponse {
    return TodoResponse(
        id= id!!,
        title = title,
        content = content,
        writeDate = writeDate,
        author = author
    )
}

