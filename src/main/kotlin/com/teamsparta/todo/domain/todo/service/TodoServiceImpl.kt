package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.exception.NotAuthenticationException
import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.NotFoundException
import com.teamsparta.todo.domain.todo.dto.*
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.toResponse
import com.teamsparta.todo.domain.todo.model.toResponseWithComments
import com.teamsparta.todo.domain.todo.repository.TodoRepositoryImpl
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import com.teamsparta.todo.domain.user.model.User
import com.teamsparta.todo.domain.user.repository.UserRepository
import com.teamsparta.todo.infra.aop.StopWatch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) : TodoService {

    override fun searchTodoList(title: String): List<TodoResponse>? {
        return todoRepository.searchTodoListByTitle(title).map { it.toResponse() }
    }


    override fun getAllTodoList(
        author: String?, pageable: Pageable): Page<TodoResponse> {
        if (author != null) {
            val pageTodo: Page<Todo> = todoRepository.findByAuthor(author, pageable)
            return pageTodo.map { it.toResponse() }
        } else {
            val pageTodo: Page<Todo> = todoRepository.findAll(pageable)
            return pageTodo.map { it.toResponse() }
        }
    }


    override fun getTodoByIdWithComment(todoId: Long): TodoWithCommentResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException("todo", todoId)
        return todo.toResponseWithComments()
    }


    @StopWatch
    override fun createTodo(request: CreateTodoRequest, userId: Long): TodoResponse {
        val user: User? = userRepository.findByIdOrNull(userId)
        val todo = Todo(
            title = request.title,
            content = request.content,
            author = request.author,
            user = user
        )
        user?.todos?.add(todo)
        return todoRepository.save(todo).toResponse()
    }

    @StopWatch
    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest, userId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException("todo", todoId)
        if (userId != todo.user!!.id) throw NotAuthenticationException("todo")
        todo.title = request.title
        todo.content = request.content
        todo.author = request.author
        todo.writeDate = LocalDateTime.now()
        return todoRepository.save(todo).toResponse()
    }


    @StopWatch
    override fun deleteTodo(todoId: Long, userId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException("todo", todoId)
        if (userId != todo.user!!.id) throw NotAuthenticationException("todo")
        todoRepository.delete(todo)
        val comment = commentRepository.findByTodoId(todoId)
        commentRepository.deleteAll(comment)
    }

    @Transactional
    override fun isCompleteTodo(todoId: Long, request: IsCompleteTodoRequest, userId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException("todo", todoId)
        if (userId != todo.user!!.id) throw NotAuthenticationException("todo")
        todo.status = request.status
        return todo.toResponse()
    }


}