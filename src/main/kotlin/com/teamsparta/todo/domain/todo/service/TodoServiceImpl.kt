package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.comment.dto.CommentResponse
import com.teamsparta.todo.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todo.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.exception.TodoNotFoundException
import com.teamsparta.todo.domain.todo.dto.IsCompleteTodoRequest
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.todo.model.toResponse
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository
) : TodoService {


    override fun getAllTodoList(pageable: Pageable): Page<TodoResponse> {
        val pageTodo: Page<Todo> = todoRepository.findAll(pageable)
        return pageTodo.map { it.toResponse() }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw TodoNotFoundException(todoId)
        return todo.toResponse()
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
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw TodoNotFoundException(todoId)
        todo.title = request.title
        todo.content = request.content
        todo.author = request.author
        todo.writeDate = LocalDateTime.now()
        return todoRepository.save(todo).toResponse()
    }


    override fun deleteTodo(todoId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw TodoNotFoundException(todoId)
        todoRepository.delete(todo)
    }

    @Transactional
    override fun isCompleteTodo(todoId:Long, request: IsCompleteTodoRequest):TodoResponse{
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw TodoNotFoundException(todoId)
        todo.status = request.status
        return todo.toResponse()
    }
}