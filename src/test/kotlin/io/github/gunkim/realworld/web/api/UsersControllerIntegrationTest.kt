package io.github.gunkim.realworld.web.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.gunkim.realworld.application.AuthenticationService
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.github.gunkim.realworld.web.api.user.model.request.UserAuthenticateRequest
import io.github.gunkim.realworld.web.api.user.model.request.UserRegistrationRequest
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

private const val AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION
private const val BEARER_TOKEN_PREFIX = "Bearer "

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Tags("Integration Test")
@DisplayName("Users Controller - Integration Test")
class UsersControllerIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val createUserService: CreateUserService,
    @Autowired private val authenticationService: AuthenticationService,
    @Autowired private val authenticationUserService: AuthenticateUserService,
    @Autowired private val objectMapper: ObjectMapper,
) : StringSpec({
    val rawPassword = "password"
    val user = createUserService.createUser(
        email = "gunkim@example.com",
        username = "gunkim",
        encodedPassword = authenticationUserService.encodePassword(rawPassword)
    )
    val token = authenticationService.createToken(user.uuid)
    val authorizationToken = "$BEARER_TOKEN_PREFIX$token"

    "GET /api/users - Return authenticated user's details" {
        mockMvc.get("/api/users") {
            header(AUTHORIZATION_HEADER, authorizationToken)
        }.andExpect {
            status { isOk() }
            jsonPath("$.user.email") { value(user.email) }
            jsonPath("$.user.username") { value(user.name) }
            jsonPath("$.user.bio") { value(user.bio) }
            jsonPath("$.user.image") { value(user.image) }
        }
    }

    "POST /api/users/login - Allow user login with valid credentials" {
        val request = UserAuthenticateRequest(email = user.email, password = rawPassword)
        val requestBody = mapOf("user" to request)
        val requestJson = objectMapper.writeValueAsString(requestBody)

        mockMvc.post("/api/users/login") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.user.email") { value(user.email) }
            jsonPath("$.user.token") { exists() }
            jsonPath("$.user.username") { value(user.name) }
        }
    }

    "POST /api/users - Register a new user" {
        val request = UserRegistrationRequest(
            email = "test@example.com",
            username = "test",
            password = "test-password"
        )
        val requestBody = mapOf("user" to request)
        val requestJson = objectMapper.writeValueAsString(requestBody)

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.user.email") { value(request.email) }
            jsonPath("$.user.username") { value(request.username) }
            jsonPath("$.user.token") { exists() }
            jsonPath("$.user.bio") { doesNotExist() }
            jsonPath("$.user.image") { doesNotExist() }
        }
    }
})