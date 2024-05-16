package com.teamsparta.todo.domain.todos.controller

import com.teamsparta.todo.domain.todos.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todos.dto.TodoResponse
import com.teamsparta.todo.domain.todos.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.todos.service.TodoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/todos")
@RestController
class TodoController(
    val todoService: TodoService
) {
    @GetMapping
    fun getTodos():ResponseEntity<List<TodoResponse>>{
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodoList())
    }
    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId:Long):ResponseEntity<TodoResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId))
    }
    @PostMapping
    fun createTodo(@RequestBody request: CreateTodoRequest):ResponseEntity<TodoResponse>{
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(request))
    }
    @PutMapping("/{todoId}")
    fun updateTodo(@PathVariable todoId:Long,@RequestBody request: UpdateTodoRequest):ResponseEntity<TodoResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId,request))
    }
    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable todoId:Long):ResponseEntity<Unit>{
        todoService.deleteTodo(todoId)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}