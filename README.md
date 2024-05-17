# 과제 - Todo(REST API 구현)

## 🔍 진행방식


- 과제는  Lv1(필수 구현 사항)부터 Lv4(Lv2,3,4는 선택 구현 기능)까지로 구성되어 있다.
- Lv1(필수 구현 사항)을 우선한다. (Spring,REST API에 친숙해지기 위한 연습 중점)

## ✉️ 과제 제출 방법

- 과제 구현을 완료한 후 GitHub을 통해 제출해야 한다.
- 제출 기한 : 05/17(금) 14시까지
   

## ✔️ 환경 설정

- Language : Kotlin
- IDLE : IntelliJ
- JDK : 17.0.11 (tumurin-17)


## 🚀 기능 요구사항

 ### 필수 구현 기능

-- [ ]  할일카드 작성 기능
        
    - `할 일 제목`, `할일 내용`, `작성일`, `작성자 이름` 을 저장할 수 있습니다.
    - 저장된 할 일의 정보를 반환 받아 확인할 수 있습니다.
- [ ]  선택한 할 일 조회 기능
        
    - 선택한 할 일의 정보를 조회할 수 있습니다.
    - 반환 받은 할 일 정보에는 `할 일 제목`,`할일 내용`, `작성일`, `작성자 이름`정보가 들어있습니다.
- [ ]  할 일카드 목록 조회 기능
        
    - 등록된 할 일 전체를 조회할 수 있습니다.
    - 조회된 할 일 목록은 작성일 기준 내림차순으로 정렬 되어있습니다.
- [ ]  선택한 할 일 수정 기능
        
    - 선택한 할 일의 `할 일 제목`, `작성자명`, `작성 내용`을 수정할 수 있습니다.
    - 수정된 할 일의 정보를 반환 받아 확인할 수 있습니다.
- [ ]  선택한 할 일 삭제 기능
    - 선택한 게시글을 삭제할 수 있습니다.
      
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
![image](https://github.com/Kozy5/Todo/assets/96171308/ba602e2e-b4b3-4bed-a23a-0de2c0391bb5)

## API 명세서
|Description|Method|URL|Request(body)|Response
|---|---|---|---|---|
|Todo 생성|`POST`|`/todos`|`CreateTodoRequest`|`TodoResponse`|
|Todo 목록 조회|`GET`|`/todos`||`Page<TodoResponse>`|
|Todo 단건 조회|`GET`|`/todos/{todoId}`||`TodoResponse`|
|Todo 수정|`PUT`|`/todos/{todoId}`|`UpdateTodoRequest`|`TodoResponse`|
|Todo 삭제|`DELETE`|`/todos/{todoId}`|||

## 기능
### Todo 생성
[생성]     
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/3e4d6594-9391-4ce2-94f8-be9baca00fa5)

[응답]   
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/3c734c69-296e-4ed9-ac2c-d54fcfb4e546)
`Response : HttpStatus CREATED(201)`
<br/>


### Todo 목록 조회
[목록 조회]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/7ac5410f-a2fe-477e-8c69-5900d1e3cc6c)
Swegger에서 @PageableDefault를 반영하기 위해 @ParameterObject사용

기본값 
```kotlin
@PageableDefault(size = 10, sort = ["writeDate"], direction = Sort.Direction.DESC)
```
[응답]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/c13169d3-9357-444e-91b6-cea6770b623d)

`Response : HttpStatus OK(200)`
<br/>


### Todo 단건 조회
[todoId 11번 조회]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/6cc23923-3076-43ef-86b0-12e5b9e0d1c3)

[응답]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/ade8e21a-e5e5-481d-aa68-5386a8d31be1)

`Response : HttpStatus OK(200)`
<br/>

### Todo 수정
[todoId 11번 수정]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/bb588d52-d581-42d5-83ca-24225c2dfa1b)


![image](https://github.com/Kozy5/Todo/assets/96171308/1da5efdb-9b2c-4c6e-b486-30cdd7f81ede)


`Response : HttpStatus OK(200)`   
<br/>
  
[업데이트 반영 된 모습]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/6c82bc56-3c88-4bdb-a2f2-c2f661245c9b)
<br/>



### Todo 삭제
[todoId 11번 삭제]   
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/0ecc6fe8-9a1a-4605-aad1-d44b7471464c)

[응답]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/0c504450-0a21-420e-813a-5d3ccf2a698e)

`Response : HttpStatus NO_CONTENT(204)`
<br/>

[삭제 반영 된 모습]
<br/>
![image](https://github.com/Kozy5/Todo/assets/96171308/8a524874-68a1-4b17-ad94-4a26464eca6e)


