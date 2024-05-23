package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.repository.CommentRepository
import com.teamsparta.todo.domain.exception.NotFoundException
import com.teamsparta.todo.domain.todo.dto.*
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.toResponse
import com.teamsparta.todo.domain.todo.model.toResponseWithComments
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository
) : TodoService {


    override fun getAllTodoList(author: String?, pageable: Pageable): Page<TodoResponse> {
        if (author != null) {
            val pageTodo: Page<Todo> = todoRepository.findByAuthor(author, pageable)
            return pageTodo.map { it.toResponse() }
        } else {
            val pageTodo: Page<Todo> = todoRepository.findAll(pageable)
            return pageTodo.map { it.toResponse() }
        }
    }
    @Transactional
    override fun getTodoByIdWithComment(todoId: Long): TodoWithCommentResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException(todoId)
        return todo.toResponseWithComments()
    }


    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        val todo = Todo(
            title = request.title,
            content = request.content,
            author = request.author
        )
        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException(todoId)
        todo.title = request.title
        todo.content = request.content
        todo.author = request.author
        todo.writeDate = LocalDateTime.now()
        return todoRepository.save(todo).toResponse()
    }


    override fun deleteTodo(todoId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException(todoId)
        todoRepository.delete(todo)
    }

    @Transactional
    override fun isCompleteTodo(todoId: Long, request: IsCompleteTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException(todoId)
        todo.status = request.status
        return todo.toResponse()
    }
}