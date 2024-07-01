# 과제 - Todo(개선 과제)

## 🔍 진행방식


1. Controller, Service 패키지 내 클래스 개선
2. JPA 심화 기술을 사용하여 검색기능 고도화
3. 코드를 체크할 수 있는 테스트 코드 작성
4. AWS 를 활용한 기능 추가 및 배포

- 총 4개의 step중 step3에 일부까지만 진행(UserControllerTest만 진행)


## ✉️ 과제 제출 방법

- 과제 구현을 완료한 후 GitHub을 통해 제출해야 한다.
- 제출 기한 : 07/01(월) 14시까지
   

## ✔️ 환경 설정

- Language : Kotlin
- IDLE : IntelliJ
- JDK : 17.0.11 (tumurin-17)


## 🚀 기능 요구사항
### [ 이번 과제 ]
- 총 4개의 step중 step3에 일부까지만 진행(UserControllerTest만 진행)
 ###  Controller, Service 패키지 내 클래스 개선 (Step 1)
 - [ ]  **Controller, Service 패키지 내 클래스 개선**
        
    - 1.Controller Advice 로 예외 공통화 처리하기
    - 2.Service 인터페이스와 구현체 분리하여 추상화 하기

##### Controller Advice 로 예외 공통화 처리하기
``` kotlin
package com.teamsparta.todo.domain.exception

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun handleModelNotFoundException(e: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(message = e.message, "404"))
    }

    @ExceptionHandler(NotAuthenticationException::class)
    fun handleNotAuthenticatedException(e: NotAuthenticationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(message = e.message,"401"))
    }


    @ExceptionHandler(InvalidCredentialException::class)
    fun handleInvalidCredentialException(e: InvalidCredentialException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message, "400"))
    }
}
```
위와 같이 GlobalExceptionHandler(RestControllerAdvice)를 통해 예외 공통화 처리

##### Service 인터페이스와 구현체 분리하여 추상화 하기
- Todo, Comment, User 모두 Service 부분을 인터페이스와 구현체(ServiceImpl)로 구성하였습니다.


- [ ]  **CustomException 정의 및 SpringAOP 적용**
        
    - 1.CustomException 정의

    - 2.Spring AOP 적용
     
##### CustomException 정의
- RuntimeException을 상속받는  NotFoundException이라는 CustomException을 만들었습니다.
- 
##### Spring AOP 적용
- StopWatch AOP를 만들어 Todo Service 로직 중 생성(createTodo), 수정(UpdateTodo), 삭제(deleteTodo) 3가지 기능에 대해 실행 속도를 확인하는데에 사용했습니다.
     
 ###  JPA 심화 기술을 사용하여 검색기능 고도화 (Step 2)
  - [ ]  **QueryDSL 을 사용하여 검색 기능 만들기**
       - 다양한 조건을 동적 쿼리로 처리하는 부분이 검색기능입니다.


- [ ]  **Pageable 을 사용하여 페이징 및 정렬 기능 만들기**
```kotlin
package com.teamsparta.todo.domain.todo.repository

...~

@Repository
class TodoRepositoryImpl : QueryDslSupport(), CustomTodoRepository {

    private val todo = QTodo.todo
    private val user = QUser.user

...~
    override fun findByPageableAndStatus(pageable: Pageable, status: Boolean?): Page<Todo> {
        val whereClause = BooleanBuilder()
        status?.let{ whereClause.and(todo.status.eq(status))}

        val totalCount = queryFactory.select(todo.count()).from(todo).where(whereClause).fetchOne() ?: 0L

        val query = queryFactory.selectFrom(todo)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        if(pageable.sort.isSorted){
            when(pageable.sort.first()?.property){
                "writeDate" -> query.orderBy(todo.writeDate.asc())
                "title" -> query.orderBy(todo.id.asc())
                else -> query.orderBy(todo.id.desc())
            }
        }

        val contents = query.fetch()

        return PageImpl(contents, pageable, totalCount)
    }


}
```


- [ ]  **다양한 조건을 동적 쿼리로 처리하기**

```kotlin
package com.teamsparta.todo.domain.todo.repository

...~
@Repository
class TodoRepositoryImpl : QueryDslSupport(), CustomTodoRepository {

    private val todo = QTodo.todo
    private val user = QUser.user

    override fun searchTodoList(
        pageable: Pageable,
        title: String?,
        author: String?,
        status: Boolean?,
        daysAgo: Long?
    ): Page<Todo> {

        val totalCount = queryFactory.select(todo.count())
            .from(todo)
            .where(
                titleContains(title),
                authorEq(author),
                statusEq(status),
                createdAfter(daysAgo)
            )
            .fetchOne() ?: 0L

        val sort = if(pageable.sort.isSorted){
            when(pageable.sort.first()?.property){
                "writeDate" -> todo.writeDate.asc()
                "title" -> todo.id.asc()
                else -> todo.id.desc()
            }
        }else{
            todo.id.desc()
        }

        val todoList = queryFactory.selectFrom(todo)
            .leftJoin(todo.user, user).fetchJoin()
            .where(
                titleContains(title),
                authorEq(author),
                statusEq(status),
                createdAfter(daysAgo)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(sort)
            .fetch()


        return PageImpl(todoList, pageable, totalCount)
    }

    private fun titleContains(title: String?): BooleanExpression? {
        return if (title != null) {
            todo.title.containsIgnoreCase(title)
        } else {
            null
        }
    }

    private fun authorEq(author: String?): BooleanExpression? {
        return if (author != null) {
            todo.user.nickname.eq(author)
        } else {
            null
        }
    }

    private fun statusEq(status: Boolean?): BooleanExpression? {
        return if (status != null) {
            todo.status.eq(status)
        } else {
            null
        }
    }

    private fun createdAfter(daysAgo: Long?): BooleanExpression? {
        return if (daysAgo != null) {
            todo.writeDate.after(LocalDateTime.now().minusDays(daysAgo))
        } else {
            null
        }
    }
...~
}
```
 ###  코드를 체크할 수 있는 테스트 코드 작성 (Step 3)
  - [ ]  ** Controller 테스트 코드 작성하기**
        - UserControllerTest만 진행
```kotlin
package com.teamsparta.todo.domain.user.controller

...~
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
@ActiveProfiles("test")
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc, private val jwtPlugin: JwtPlugin,

    @MockkBean
    private val userService: UserServiceImpl
) : DescribeSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    describe("POST /signup 은") {
        context("유효한 회원가입 요청을 보내면") {
            it("201 status code와 UserResponse를 반환해야한다.") {
                val email = "email"
                val password = "password"
                val nickname = "nickname"


                every { userService.signUp(any()) } returns UserResponse(
                    id = 1L,
                    email = email,
                    nickname = nickname,

                )

                val requestBody =
                    """{"email":"$email" "nickname":"$nickname","password":"$password"}"""

                val result = mockMvc.perform(
                    post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                ).andReturn()

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.getContentAsString(Charsets.UTF_8),
                    UserResponse::class.java
                )

                result.response.status shouldBe 201

                responseDto.id shouldBe 1L
                responseDto.nickname shouldBe nickname
            }
        }

        context("중복된 닉네임으로 회원가입 요청을 보내면") {
            it("400 status code와 ErrorResponse를 반환해야한다.") {
                val email = "email"
                val password = "password"
                val nickname = "nickname"

                every { userService.signUp(any()) } throws IllegalStateException("이미 존재하는 이메일입니다.")

                val requestBody =
                    """{"email":"$email" "nickname":"$nickname","password":"$password"}"""

                val result = mockMvc.perform(
                    post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                ).andReturn()

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.getContentAsString(Charsets.UTF_8),
                    ErrorResponse::class.java
                )

                result.response.status shouldBe 400
                responseDto.message shouldBe "이미 존재하는 이메일입니다."
                responseDto.errorCode shouldBe "400"
            }
        }
    }

    describe("POST /login 은") {
        context("정상적인 로그인 요청을 보내면") {
            it("200 status code와 토큰을 생성해 쿠키와 LoginResponse로 반환해야한다.") {
                val email = "email"
                val password = "password"

                val accessToken = jwtPlugin.generateAccessToken(1.toString(), email)

                val responseSlot = slot<HttpServletResponse>()

                every { userService.login(any(), capture(responseSlot)) } answers {
                    val response = responseSlot.captured
                    val cookie = Cookie("accessToken", accessToken)
                        .apply {
                            path = "/"
                            maxAge = 2 * 24 * 60 * 60
                            isHttpOnly = true
                        }

                    response.addCookie(cookie)

                    LoginResponse(
                        accessToken = accessToken
                    )
                }

                val requestBody = """{"email":"$email","password":"$password"}"""

                val result = mockMvc.perform(
                    post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                ).andExpect(status().isOk)
                    .andExpect(cookie().exists("accessToken"))
                    .andExpect(cookie().value("accessToken", accessToken))
                    .andReturn()

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.getContentAsString(Charsets.UTF_8),
                    LoginResponse::class.java
                )

                responseDto.accessToken shouldBe accessToken

            }
        }

        context("존재하지않는 이메일로 로그인 요청을 보내면") {
            it("404 status code와 ErrorResponse를 반환해야한다.") {
                val email = "email"
                val password = "password"

                val responseSlot = slot<HttpServletResponse>()

                every {
                    userService.login(
                        any(),
                        capture(responseSlot)
                    )
                } throws NotFoundException("User", email)

                val requestBody = """{"email":"$email","password":"$password"}"""

                val result = mockMvc.perform(
                    post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                ).andReturn()

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.getContentAsString(Charsets.UTF_8),
                    ErrorResponse::class.java
                )

                result.response.status shouldBe 404
                responseDto.message shouldBe "User not found with given id:$email"
                responseDto.errorCode shouldBe "404"
            }
        }

        context("잘못된 비밀번호로 로그인 요청을 보내면") {
            it("400 status code와 ErrorResponse를 반환해야한다.") {
                val email = "email"
                val password = "password"

                val responseSlot = slot<HttpServletResponse>()

                every {
                    userService.login(
                        any(),
                        capture(responseSlot)
                    )
                } throws InvalidCredentialException("이메일 혹은 비밀번호를 확인해주세요")

                val requestBody = """{"email":"$email","password":"$password"}"""

                val result = mockMvc.perform(
                    post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                ).andReturn()

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.getContentAsString(Charsets.UTF_8),
                    ErrorResponse::class.java
                )

                result.response.status shouldBe 400
                responseDto.message shouldBe "Invalid Credential: 이메일 혹은 비밀번호를 확인해주세요"
                responseDto.errorCode shouldBe "400"
            }
        }
    }
})
```


 

### [ 이전 과제 ]
 ### 필수 구현 기능 (Step 1) / domain\todo\controller\TodoContoroller.kt 

- [ ]  **할일카드 작성 기능**
        
    - "할 일 제목", "할 일 내용", "작성일", "작성자 이름"  저장
    - 저장된 할 일의 정보 반환

- [ ]  **선택한 할 일 조회 기능**
        
    - 선택한 할 일 정보 조회
    - 반환 받은 할 일 정보에는 `할 일 제목`,`할일 내용`, `작성일`, `작성자 이름`정보가 들어있습니다.
     

- [ ]  **할 일카드 목록 조회 기능**
        
    - 등록된 할 일 전체 조회
    - 조회된 할 일 목록은 작성일 기준 내림차순 정렬
- [ ]  **선택한 할 일 수정 기능**
        
    - 선택한 할 일의 `할 일 제목`, `작성자명`, `작성 내용` 수정
    - 수정된 할 일의 정보를 반환
      
- [ ]  **선택한 할 일 삭제 기능**
    - 선택한 게시글 삭제
     
### 선택 구현 기능(Step 2)

- [ ]  **할일카드 완료 기능 API**    
    - `완료 여부` 기본값은 FALSE
    - 완료처리 한 할일카드는 `완료 여부`필드 TRUE
      
##### 기본값 FALSE
```kotlin
package com.teamsparta.todo.domain.todo.model

@Column(name = "status")
    var status: Boolean = false
```
##### 완료처리 한 할일카드는 `완료 여부`필드 TRUE
- Service
```kotlin
package com.teamsparta.todo.domain.todo.service

@Transactional
    override fun isCompleteTodo(todoId: Long, request: IsCompleteTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException(todoId)
        todo.status = request.status
        return todo.toResponse()
    }
```
- Controller
```kotlin
package com.teamsparta.todo.domain.todo.controller

@PatchMapping("/{todoId}")
    fun isCompleteTodo(
        @PathVariable todoId: Long,
        @RequestBody isCompleteTodoRequest: IsCompleteTodoRequest
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.isCompleteTodo(todoId, isCompleteTodoRequest))
    }
```

- [ ]  **댓글 작성 API**
    - 댓글 작성 시 `작성자 이름`과 `비밀번호`를 함께 받기
    - 선택한 할 일의 DB 저장 유무 확인
    - 선택한 할 일이 DB에 저장되어 있다면 댓글 등록, 반환
    - 응답에서 `비밀번호`는 제외하고 반환

##### 댓글 작성 시 `작성자 이름`과 `비밀번호`를 함께 받기
- Request DTO
```kotlin
package com.teamsparta.todo.domain.comment.dto

data class CreateCommentRequest(
    val content: String,
    val author: String,
    val password: String
)
```

##### 선택한 할 일의 DB 저장 유무 확인,선택한 할 일이 DB에 저장되어 있다면 댓글 등록, 반환
- Service
```kotlin
package com.teamsparta.todo.domain.comment.service

override fun createComment(todoId: Long, request: CreateCommentRequest): CommentResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw NotFoundException()
        val comment = Comment(
            content = request.content,
            author = request.author,
            password = request.password,
            todo = todo
        )
        todo.comments.add(comment)
        return commentRepository.save(comment).toResponse()
    }
```
- Controller
```kotlin
package com.teamsparta.todo.domain.comment.controller

@PostMapping()
    fun createComment(
        @PathVariable todoId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(todoId, createCommentRequest))
    }
```
##### 응답에서 `비밀번호`는 제외하고 반환
- Response DTO
```kotlin
package com.teamsparta.todo.domain.comment.dto

data class CommentResponse(
    val id: Long,
    val content: String,
    val author: String
)
```
          
- [ ]  **댓글 수정 API**
    - 선택한 댓글의 DB 저장 유무 확인
    - 선택한 댓글이 있으면서 `작성자 이름`과 `비밀번호`를 함께 받아 저장된 값과 일치하면 댓글 수정, 반환
      
##### 선택한 댓글의 DB 저장 유무 확인, 선택한 댓글이 있으면서 `작성자 이름`과 `비밀번호`를 함께 받아 저장된 값과 일치하면 댓글 수정, 반환
- Service
```kotlin
package com.teamsparta.todo.domain.comment.service

override fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw NotFoundException()
        if (!comment.isValidAuthor(request.author) || !comment.isValidPassword(request.password)) throw InformationDifferentException()
        comment.content = request.content
        return commentRepository.save(comment).toResponse()
    }
```
- Controller
```kotlin
package com.teamsparta.todo.domain.comment.controller

@PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable todoId: Long,@PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(todoId, commentId, updateCommentRequest))
    }
```

          
- [ ]  **댓글 삭제 API**
    - 선택한 댓글의 DB 저장 유무 확인
    - 선택한 댓글이 있으면서 `작성자 이름`과 `비밀번호`를 함께 받아 저장된 값과 일치하면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
      
##### 선택한 댓글의 DB 저장 유무 확인, 선택한 댓글이 있으면서 `작성자 이름`과 `비밀번호`를 함께 받아 저장된 값과 일치하면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
- Service
```kotlin
package com.teamsparta.todo.domain.comment.service

override fun deleteComment(todoId: Long, commentId: Long, request: DeleteCommentRequest) {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw NotFoundException()
        if (!comment.isValidAuthor(request.author) || !comment.isValidPassword(request.password)) throw InformationDifferentException()
        commentRepository.delete(comment)
    }
```
- Controller
```kotlin
package com.teamsparta.todo.domain.comment.controller

@DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        deleteCommentRequest: DeleteCommentRequest
    ): ResponseEntity<Unit> {
        commentService.deleteComment(todoId, commentId, deleteCommentRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
```
      
- [ ]  **선택한 할 일 조회 기능 응답에 연관된 댓글 목록 추가하기**
    - **STEP 1**에서 만든 선택한 할 일 조회 api의 응답에 연관된 댓글 목록 추가
    - 연관되지 않은 댓글은 포함 X
    - 할 일 목록 api에는 추가 X

##### 연관 관계
- 할 일 Entity
```kotlin
package com.teamsparta.todo.domain.todo.model

@Entity
@Table(name = "todo")
class Todo(
~
@OneToMany(mappedBy = "todo", fetch = FetchType.EAGER,cascade = [CascadeType.ALL], orphanRemoval = true,)
    val comments: MutableList<Comment> = mutableListOf()
~
)
```
- 댓글 Entity
```kotlin
package com.teamsparta.todo.domain.comment.model

@Entity
@Table(name = "comment")
class Comment(
~
@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    val todo: Todo
~
)
```

##### 선택한 할 일 조회 기능 응답에 연관된 댓글 목록 추가하기
 - 할일 Entity 확장함수
```kotlin
package com.teamsparta.todo.domain.todo.model

fun Todo.toResponseWithComments():TodoWithCommentResponse{
    val commentsInDto: MutableList<CommentResponse> = mutableListOf()
    for(i in comments){
        commentsInDto += i.toResponse()
    }
    return TodoWithCommentResponse(
        id = id!!,
        title = title,
        content = content,
        author = author,
        status = status,
        comments = commentsInDto,
        writeDate = writeDate
    )
}
```
양방향 관계에 순환참조에 문제에 대한 임시 조치로 Comment Entity-> DTO 변환 후 List에 담아서 Response
     
  ### 선택 구현 기능(Step 3)

- [ ]  **할 일 목록 api에 작성일 기준 오름차순, 내림차순 정렬 기능 추가**
    - api를 요청할 때 정렬 기준(오름차순, 내림차순)을 포함하기
    - 정렬 기준을 통해 정렬한 할 일 목록 반환
     
##### 작성일 기준 내림차순,오름차순 Request 받기
- Controller
```kotlin
package com.teamsparta.todo.domain.todo.controller

 @GetMapping
    fun getTodos(
        @RequestParam(required = false) author: String?,
        @ParameterObject
        @PageableDefault(size = 10, sort = ["writeDate"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<Page<TodoResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodoList(author, pageable))
    }
```
      
- [ ]  **할 일 목록 api에 작성자 기준 필터 기능 추가**
    - api를 요청할 때 작성자 이름 포함
    - 작성자 이름이 일치하는 할 일 목록 반환
     
- Controller
```kotlin
package com.teamsparta.todo.domain.comment.controller

@GetMapping
    fun getTodos(
        @RequestParam(required = false) author: String?,
        @ParameterObject
        @PageableDefault(size = 10, sort = ["writeDate"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<Page<TodoResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodoList(author, pageable))
    }
```
- Repository
```kotlin
package com.teamsparta.todo.domain.todo.repository

interface TodoRepository : JpaRepository<Todo, Long> {
    fun findByAuthor(author: String, pageable: Pageable): Page<Todo>
}
```
- Service
```kotlin
package com.teamsparta.todo.domain.todo.service

override fun getAllTodoList(author: String?, pageable: Pageable): Page<TodoResponse> {
        if (author != null) {
            val pageTodo: Page<Todo> = todoRepository.findByAuthor(author, pageable)
            return pageTodo.map { it.toResponse() }
        } else {
            val pageTodo: Page<Todo> = todoRepository.findAll(pageable)
            return pageTodo.map { it.toResponse() }
        }
    }
```

- [ ]  **할 일 작성, 수정 api에 validation 추가**
    - 할 일 작성,수정 시, 할일 제목 1자 이상, 200자 이내 유효성 검사
    - 할 일 본문 1자 이상 1000자 이내 유효성 검사
    - 조건 미충족 시 기능 실패 응답

- Dependency
```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

dependencies {
~
implementation("org.springframework.boot:spring-boot-starter-validation")
~
}
```
- 할일 작성(생성) DTO
```kotlin
package com.teamsparta.todo.domain.todo.dto

data class CreateTodoRequest(
    @field:Size(min = 1, max = 200)
    val title: String,

    @field:Size(min = 1, max = 1000)
    val content: String?,

    @field:NotBlank
    val author: String
)
```
- 할일 수정 DTO
```kotlin
package com.teamsparta.todo.domain.todo.dto

data class UpdateTodoRequest(
    @field:Size(min = 1, max = 200)
    val title: String,

    @field:Size(min = 1, max = 1000)
    val content: String?,

    @field:NotBlank
    val author: String
)
```
- Controller / 작성,수정 기능 @Valid 적용 
```kotlin
package com.teamsparta.todo.domain.todo.controller
~
@PostMapping
    fun createTodo(@Valid @RequestBody createTodoRequest: CreateTodoRequest): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(createTodoRequest))
    }

@PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @Valid @RequestBody updateTodoRequest: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, updateTodoRequest))
    }
~
```

- [ ]  **"ResponseEntity"를 사용하여 api의 응답으로 적절한 코드 반환.** / domain\todo\controller\TodoController.kt 각 기능 별 return값에 status관련 코드 참조.
- 조회기능 성공: status 200 OK
- 작성기능 성공: status 201 Created
- 수정기능 성공: status 200 OK
- 삭제기능 성공: status 204 No Content
- 작성, 수정기능 실패: status 400 Bad Request
  


## Entity Relationship Diagram(ERD)
![image](https://github.com/Kozy5/Todo/assets/96171308/dedaf69e-7498-49d6-9ba5-fff8b05977ce)



## API 명세서
|Description|Method|URI|Request(body)|Response
|---|---|---|---|---|
|Todo 생성|`POST`|`/todos`|`CreateTodoRequest`|`TodoResponse`|
|Todo 목록 조회|`GET`|`/todos`||`Page<TodoResponse>`|
|Todo 단건 조회|`GET`|`/todos/{todoId}`||`TodoResponse`|
|Todo 수정|`PUT`|`/todos/{todoId}`|`UpdateTodoRequest`|`TodoResponse`|
|Todo 삭제|`DELETE`|`/todos/{todoId}`|||
|Comment 생성|`POST`|`/todos/{todoId}/comments`|`CreateCommentRequest`|`CommentResponse`|
|Comment 수정|`PUT`|`/todos/{todoId}/comments/{commentsId}`|`UpdateCommentRequest`|`CommentResponse`|
|Comment 삭제|`DELETE`|`/todos/{todoId}/comments/{commentsId}`|||
|User 회원가입|`POST`|`/signup`|`SignUpRequest`|`TodoResponse`|
|User 로그인|`POST`|`/login`|`LoginRequest`|`LoginResponse`|
|User 내 정보 수정|`PUT`|`/info`|`UpdateUserProfileRequest`|`TodoResponse`|


