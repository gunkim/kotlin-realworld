package io.github.gunkim.realworld.web.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.gunkim.realworld.share.IntegrationTest
import io.github.gunkim.realworld.web.api.user.model.request.UserUpdateRequest
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.DisplayName
import io.kotest.core.test.TestCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.put

@DisplayName("User Controller - Integration Test")
class UserControllerIntegrationTest(
    @Autowired private val objectMapper: ObjectMapper,
) : IntegrationTest() {
    private lateinit var authorizationToken: String

    override suspend fun beforeEachTest(testCase: TestCase) {
        val rawPassword = "password"

        val (_, authToken) = createUser(
            email = "gunkim@example.com",
            username = "gunkim",
            password = rawPassword
        )

        this.authorizationToken = authToken
    }

    init {
        "PUT /api/user - Update user information" {
            val updateRequest = UserUpdateRequest(
                email = "updated@example.com",
                username = "updatedUser",
                password = "new_password",
                image = "http://example.com/updated.png",
                bio = "Updated Bio"
            )
            val requestBody = mapOf("user" to updateRequest)
            val requestJson = objectMapper.writeValueAsString(requestBody)

            mockMvc.put("/api/user") {
                header(HttpHeaders.AUTHORIZATION, authorizationToken)
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