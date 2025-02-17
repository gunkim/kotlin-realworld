package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.share.IntegrationTest
import io.github.gunkim.realworld.web.api.user.model.request.UserAuthenticateRequest
import io.github.gunkim.realworld.web.api.user.model.request.UserRegistrationRequest
import io.kotest.core.spec.DisplayName
import io.kotest.core.test.TestCase
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@DisplayName("Users Controller - Integration Test")
class UsersControllerIntegrationTest : IntegrationTest() {
    private lateinit var authorizationToken: String
    private lateinit var user: User
    val rawPassword = "password"

    override suspend fun beforeEachTest(testCase: TestCase) {
        val (user, token) = createUser(
            email = "gunkim@example.com",
            username = "gunkim",
            password = rawPassword
        )
        this.user = user
        this.authorizationToken = token
    }

    init {
        "GET /api/users - Return authenticated user's details" {
            mockMvc.get("/api/users") {
                header(HttpHeaders.AUTHORIZATION, authorizationToken)
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
            val requestJson = toJsonString(requestBody)

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
            val requestJson = toJsonString(requestBody)

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
    }
}