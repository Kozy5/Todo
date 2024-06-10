package com.teamsparta.todo.domain.user.repository


import com.teamsparta.todo.domain.user.model.UserRole
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoleRepository : JpaRepository<UserRole, Long>