package com.teamsparta.todo.domain.user.model


import com.teamsparta.todo.domain.comment.model.Comment
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.user.dto.response.UserResponse
import jakarta.persistence.*

@Entity
@Table(name = "app_user")
class User(
    @Column(name = "email", nullable = false)
    var email: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(nullable = false)
    val role: UserRole,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",cascade = [CascadeType.ALL], orphanRemoval = true)
    var todos: MutableList<Todo>? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comment>? = null,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}
fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id!!,
        nickname = nickname,
        email = email,
        role = role.name
    )
}