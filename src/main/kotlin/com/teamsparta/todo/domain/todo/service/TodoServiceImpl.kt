package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.dto.UpdateTodoRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoServiceImpl:TodoService {
    override fun getAllTodoList(): List<TodoResponse> {
//      TODO : DB에서 모든 Todo 목록 조회하여 TodoResponse 목록으로 변환 후 반환
        TODO("Not yet implemented")
    }

    override fun getTodoById(todoId: Long): TodoResponse {
//      TODO : DB에서 todoId에 해당하는 Todo를 가져와서 TodoReponse로 변환후 반환
        TODO("Not yet implemented")
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest): TodoResponse {
//      TODO : request를 Todo(Entity)로 변환 후 DB에 저장
        TODO("Not yet implemented")
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
//      TODO : DB에서 todoId에 해당하는 Todo를 가져와서 request 정보로 수정(Update) 후 DB에 저장, 결과를 TodoResponse로 변환 후 반환
        TODO("Not yet implemented")
    }

    @Transactional
    override fun deleteTodo(todoId: Long) {
//      TODO : DB에서 todoId에 해당하는 Tood를 삭제
        TODO("Not yet implemented")
    }
}