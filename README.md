# 과제 - Todo(REST API 구현)

## 🔍 진행방식


- 과제는  Step 1(필수 구현 사항)부터 Step 4(Step 2,3,4는 선택 구현 기능)까지로 구성되어 있다.
- Step 1(필수 구현 사항)을 우선한다. (Spring,REST API에 친숙해지기 위한 연습 중점 가능하면 추가적으로 진행)

## ✉️ 과제 제출 방법

- 과제 구현을 완료한 후 GitHub을 통해 제출해야 한다.
- 제출 기한 : 05/24(금) 14시까지
   

## ✔️ 환경 설정

- Language : Kotlin
- IDLE : IntelliJ
- JDK : 17.0.11 (tumurin-17)


## 🚀 기능 요구사항

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
  
      
## 과제에서 요구한 질문
1. 수정, 삭제 API의 request를 어떤 방식으로 사용 하셨나요? (param, query, body)
   -> @RequestBody
   - URL Path에 todoId를 넣어 수정/삭제할 대상 구별
   - 수정할 때 Body에 DTO(UpdateTaskRequest)로 감싸서 사용
     </br>
   
2. RESTful한 API를 설계하셨나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?
  - URI에 Reousrce의 이름만 표기 [ex) @RequestMapping("/todos") 특정 대상 조회 및 수정 삭제는 "/{todoId}"]
  - 행동적인 표현은 HTTP Method를 통해서만 진행
  - Resource 표현 데이터 구조 JSON 사용</br>
  </br>
 ( 이런 부분들은 RESTful한 API설계라고 생각합니다. )
   
3. 적절한 관심사 분리를 적용하셨나요? (Controller, Service, Repository)
   - Controller: 서버에 들어오는 Request Control
   - Entity: DB에 저장할 데이터
   - Repository: Entity에 접근
   - Service: CRUD 기능 구현
   
## Entity Relationship Diagram(ERD)
![image](https://github.com/Kozy5/Todo/assets/96171308/f22945ba-fc65-49d4-a97a-289879a78fff)


## API 명세서
|Description|Method|URI|Request(body)|Response
|---|---|---|---|---|
|Todo 생성|`POST`|`/todos`|`CreateTodoRequest`|`TodoResponse`|
|Todo 목록 조회|`GET`|`/todos`||`Page<TodoResponse>`|
|Todo 단건 조회|`GET`|`/todos/{todoId}`||`TodoResponse`|
|Todo 수정|`PUT`|`/todos/{todoId}`|`UpdateTodoRequest`|`TodoResponse`|
|Todo 삭제|`DELETE`|`/todos/{todoId}`|||
|Comment 생성|`POST`|`/comments`|`CreateCommentRequest`|`CommentResponse`|
|Comment 수정|`PUT`|`/comments/{commentsId}`|`UpdateCommentRequest`|`CommentResponse`|
|Comment 삭제|`DELETE`|`/comments/{commentsId}`|||

