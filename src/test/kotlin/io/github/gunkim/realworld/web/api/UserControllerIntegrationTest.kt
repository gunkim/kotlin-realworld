package io.github.gunkim.realworld.web.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.gunkim.realworld.application.AuthenticationService
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.github.gunkim.realworld.web.api.user.model.request.UserUpdateRequest
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import org.springframework.transaction.annotation.Transactional

private const val AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION
private const val BEARER_TOKEN_PREFIX = "Bearer "

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Tags("Integration Test")
@DisplayName("User Controller - Integration Test")
class UserControllerIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val createUserService: CreateUserService,
    @Autowired private val authenticationService: AuthenticationService,
    @Autowired private val authenticateUserService: AuthenticateUserService,
    @Autowired private val objectMapper: ObjectMapper,
) : StringSpec({
    val rawPassword = "password"
    val user = createUserService.createUser(
        email = "gunkim@example.com",
        username = "gunkim",
        encodedPassword = authenticateUserService.encodePassword(rawPassword)
    )
    val token = authenticationService.createToken(user.uuid)
    val authorizationToken = "$BEARER_TOKEN_PREFIX$token"

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
            header(AUTHORIZATION_HEADER, authorizationToken)
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
})