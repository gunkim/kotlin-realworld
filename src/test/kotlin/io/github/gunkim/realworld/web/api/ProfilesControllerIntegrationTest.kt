package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.AuthenticationService
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
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
    @Autowired private val followUserService: FollowUserService
) : StringSpec({
    extension(SpringExtension)

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

    "POST /api/profiles/:username/follow - Follow a profile" {
        mockMvc.post("/api/profiles/${targetUser.name}/follow") {
            header(AUTHORIZATION_HEADER, "$BEARER_TOKEN_PREFIX$token")
        }.andExpect {
            status { isOk() }
        }
    }

    "DELETE /api/profiles/:username/follow - Unfollow a profile" {
        followUserService.followUser(authenticatedUser.uuid, targetUser.name)

        mockMvc.delete("/api/profiles/${targetUser.name}/follow") {
            header(AUTHORIZATION_HEADER, "$BEARER_TOKEN_PREFIX$token")
        }.andExpect {
            status { isOk() }
        }
    }
})