package com.teamsparta.todo.domain.todo.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.Date

@Entity
@Table(name = "todos")
class Todo(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String? = null,

    @CreatedDate
    @Column(name = "write_date")
    var writeDate: LocalDateTime? = null,

    @Column(name = "author")
    var author: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}


