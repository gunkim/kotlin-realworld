package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.AuthenticationService
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional

private const val AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION
private const val BEARER_TOKEN_PREFIX = "Bearer "

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Tags("Integration Test")
@DisplayName("Profiles Controller - Integration Test")
class ProfilesControllerIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val createUserService: CreateUserService,
    @Autowired private val authenticationService: AuthenticationService,
    @Autowired private val authenticateUserService: AuthenticateUserService,
) : StringSpec({
    val rawPassword = "password"
    val authenticatedUser = createUserService.createUser(
        email = "authuser@example.com",
        username = "authUser",
        encodedPassword = authenticateUserService.encodePassword(rawPassword)
    )
    val targetUser = createUserService.createUser(
        email = "target@example.com",
        username = "targetUser",
        encodedPassword = authenticateUserService.encodePassword(rawPassword)
    )
    val token = authenticationService.createToken(authenticatedUser.uuid)
    val authorizationToken = "$BEARER_TOKEN_PREFIX$token"

    "GET /api/profiles/:username - Return profile without authentication" {
        mockMvc.get("/api/profiles/${targetUser.name}")
            .andExpect {
                status { isOk() }
                jsonPath("$.profile.username") { value(targetUser.name) }
                jsonPath("$.profile.bio") { value(targetUser.bio) }
                jsonPath("$.profile.image") { value(targetUser.image) }
                jsonPath("$.profile.following") { value(false) }
            }
    }

    "GET /api/profiles/:username - Return profile with authentication (not following)" {
        mockMvc.get("/api/profiles/${targetUser.name}") {
            header(AUTHORIZATION_HEADER, authorizationToken)
        }.andExpect {
            status { isOk() }
            jsonPath("$.profile.username") { value(targetUser.name) }
            jsonPath("$.profile.bio") { value(targetUser.bio) }
            jsonPath("$.profile.image") { value(targetUser.image) }
            jsonPath("$.profile.following") { value(false) }
        }
    }
})