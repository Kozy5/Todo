package com.teamsparta.todo.domain.todo.service

import com.teamsparta.todo.domain.todo.dto.CreateTodoRequest
import com.teamsparta.todo.domain.todo.dto.TodoResponse
import com.teamsparta.todo.domain.todo.repository.TodoRepository
import com.teamsparta.todo.domain.user.model.User
import com.teamsparta.todo.domain.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class TodoServiceTest(
    @Autowired private val todoService: TodoService,
    @Autowired private val todoRepository: TodoRepository,
    @Autowired private val userRepository: UserRepository
)
{
    private var userId: Long? = 0

    @BeforeEach
    fun setUp(

    )
    {
        val user = User(
            email = "email@email.com",
            nickname = "Nickname",
            password = "password"
        )
        userRepository.save(user)
        userId = user.id
    }

    @Test
    fun concurrencyIssue(

    )
    {
        val executorService = Executors.newFixedThreadPool(10)
        val cyclicBarrier = CyclicBarrier(10)
        val results = Collections.synchronizedList(mutableListOf<TodoResponse>())

        repeat(10){
            executorService.submit{
                cyclicBarrier.await()
                try{
                    val request = CreateTodoRequest(
                        title = "제목",
                        content = "내용",
                        author = "작성자"
                    )

                    val todo = todoService.createTodo(request, userId!!)
                    results.add(todo)
                } catch (e: Exception) {
                    println("Error : ${e.message}")
                }
            }
        }
        executorService.shutdown()
        executorService.awaitTermination(1, TimeUnit.MINUTES)

        println("Todo Count: ${results.size}")

        assertEquals(10,results.size)
    }
}