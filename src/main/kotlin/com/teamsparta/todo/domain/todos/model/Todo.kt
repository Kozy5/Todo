package com.teamsparta.todo.domain.todos.model

import com.teamsparta.todo.domain.todos.dto.TodoResponse
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "todo")
class Todo(
    @Column(name = "title")
    var title: String = "",

    @Column(name = "content")
    var content: String? = null,

    @Column(name = "write_date")
    var writeDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "author")
    var author: String = ""

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

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

