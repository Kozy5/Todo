package com.teamsparta.todo.domain.todos.service

import com.teamsparta.todo.domain.todos.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todos.dto.TodoResponse
import com.teamsparta.todo.domain.todos.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.todos.exception.TodoNotFoundException
import com.teamsparta.todo.domain.todos.model.Todo
import com.teamsparta.todo.domain.todos.model.toResponse
import com.teamsparta.todo.domain.todos.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository
):TodoService {


    override fun getAllTodoList(): List<TodoResponse> {
        return todoRepository.findAll().map{it.toResponse()}
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw TodoNotFoundException(todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        val todo = Todo(
            title = request.title,
            content = request.content,
            author = request.author,
            writeDate = request.writeDate
        )
        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw TodoNotFoundException(todoId)
        todo.title = request.title
        todo.content = request.content
        todo.author = request.author
        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun deleteTodo(todoId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw TodoNotFoundException(todoId)
        todoRepository.delete(todo)
    }
}