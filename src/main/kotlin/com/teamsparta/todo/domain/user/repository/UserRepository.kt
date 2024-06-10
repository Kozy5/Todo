package com.teamsparta.todo.domain.user.repository


import com.teamsparta.todo.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {

    fun existsByUserId(userId: String): Boolean
    fun findByUserId(userId: String): User?

    fun existsByEmail(email:String):Boolean
    fun findByEmail(email: String): User?
}