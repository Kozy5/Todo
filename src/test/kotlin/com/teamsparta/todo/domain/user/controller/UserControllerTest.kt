package com.teamsparta.todo.domain.user.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.teamsparta.todo.domain.exception.NotFoundException
import com.teamsparta.todo.domain.exception.dto.ErrorResponse
import com.teamsparta.todo.domain.user.dto.response.LoginResponse
import com.teamsparta.todo.domain.user.dto.response.UserResponse
import com.teamsparta.todo.domain.user.exception.InvalidCredentialException
import com.teamsparta.todo.domain.user.exception.UserIdIllegalStateException
import com.teamsparta.todo.domain.user.service.UserService
import com.teamsparta.todo.infra.security.jwt.JwtPlugin
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
@ActiveProfiles("test")
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc, private val jwtPlugin: JwtPlugin,

    @MockkBean
    private val userService: UserService
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

                every { userService.signUp(any()) } throws UserIdIllegalStateException("이미 존재하는 이메일입니다.")

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