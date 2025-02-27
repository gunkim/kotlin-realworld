package io.github.gunkim.realworld.testcase.integration

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.share.IntegrationTest
import io.github.gunkim.realworld.api.user.model.request.UserUpdateRequest
import io.kotest.core.spec.DisplayName
import io.kotest.core.test.TestCase
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put

@DisplayName("User Controller - Integration Test")
class UserControllerIntegrationTest : IntegrationTest() {
    private lateinit var authorizationToken: String
    private lateinit var authUser: User

    override suspend fun beforeEachTest(testCase: TestCase) {
        val rawPassword = "password"

        val (authUser, authToken) = createUser(
            email = "gunkim@example.com",
            username = "gunkim",
            password = rawPassword
        )

        this.authorizationToken = authToken
        this.authUser = authUser
    }

    init {
        "GET /api/user - Return authenticated user's details" {
            mockMvc.get("/api/user") {
                header(authHeaderName, authorizationToken)
            }.andExpect {
                status { isOk() }
                jsonPath("$.user.email") { value(authUser.email) }
                jsonPath("$.user.username") { value(authUser.name) }
                jsonPath("$.user.bio") { value(authUser.bio) }
                jsonPath("$.user.image") { value(authUser.image) }
            }
        }

        "PUT /api/user - Update user information" {
            val updateRequest = UserUpdateRequest(
                email = "updated@example.com",
                username = "updatedUser",
                password = "new_password",
                image = "http://example.com/updated.png",
                bio = "Updated Bio"
            )
            val requestBody = mapOf("user" to updateRequest)
            val requestJson = toJsonString(requestBody)

            mockMvc.put("/api/user") {
                header(authHeaderName, authorizationToken)
                contentType = MediaType.APPLICATION_JSON
                content = requestJson
            }.andExpect {
                status { isOk() }
                jsonPath("$.user.email") { value(updateRequest.email) }
                jsonPath("$.user.username") { value(updateRequest.username) }
                jsonPath("$.user.bio") { value(updateRequest.bio) }
                jsonPath("$.user.image") { value(updateRequest.image) }
            }
        }
    }
}