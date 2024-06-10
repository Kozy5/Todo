package com.teamsparta.todo.domain.user.model


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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    var todo: MutableList<Todo>? = null,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    val userRole: List<UserRole>? = null
}
fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id!!,
        nickname = nickname,
        email = email
    )
}