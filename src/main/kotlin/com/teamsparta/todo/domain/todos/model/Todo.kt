package com.teamsparta.todo.domain.todos.model

import com.teamsparta.todo.domain.todos.dto.TodoResponse
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

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

