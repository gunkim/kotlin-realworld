package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.IntegrationTest
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.DisplayName
import io.kotest.core.test.TestCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@Tags("Integration Test")
@DisplayName("Profiles Controller - Integration Test")
class ProfilesControllerIntegrationTest(
    @Autowired private val followUserService: FollowUserService,
) : IntegrationTest() {
    private lateinit var authenticatedUser: User
    private lateinit var profileUser: User
    private lateinit var token: String

    override suspend fun beforeEach(testCase: TestCase) {
        setupTestUsers("password")
    }

    private fun setupTestUsers(rawPassword: String) {
        val (authUser, authToken) = createUser(
            email = "gunkim@example.com",
            username = "gunkim",
            password = rawPassword
        )
        val (targetUser, _) = createUser(
            email = "target@example.com",
            username = "targetUser",
            password = rawPassword
        )

        authenticatedUser = authUser
        profileUser = targetUser
        token = authToken
    }

    private fun profileEndpoint(username: String) = "/api/profiles/$username"
    private fun followEndpoint(username: String) = "${profileEndpoint(username)}/follow"

    init {
        "GET /api/profiles/:username - Return profile without authentication" {
            mockMvc.get(profileEndpoint(profileUser.name))
                .andExpect {
                    status { isOk() }
                    jsonPath("$.profile.username") { value(profileUser.name) }
                    jsonPath("$.profile.bio") { value(profileUser.bio) }
                    jsonPath("$.profile.image") { value(profileUser.image) }
                    jsonPath("$.profile.following") { value(false) }
                }
        }
        "POST /api/profiles/:username/follow - Follow a profile" {
            mockMvc.post(followEndpoint(profileUser.name)) {
                header(HttpHeaders.AUTHORIZATION, token)
            }.andExpect {
                status { isOk() }
            }
        }
        "DELETE /api/profiles/:username/follow - Unfollow a profile" {
            followUserService.followUser(authenticatedUser.uuid, profileUser.name)
            mockMvc.delete(followEndpoint(profileUser.name)) {
                header(HttpHeaders.AUTHORIZATION, token)
            }.andExpect {
                status { isOk() }
            }
        }
    }
}