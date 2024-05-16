package com.teamsparta.todo.domain.todo.controller

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import com.teamsparta.todo.domain.todo.service.TodoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService
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
        return ResponseEntity.status(HttpStatus.OK).body(todoService.deleteTodo(todoId))
    }
}