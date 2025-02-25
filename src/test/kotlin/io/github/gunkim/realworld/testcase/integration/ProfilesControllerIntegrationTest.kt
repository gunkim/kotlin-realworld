package io.github.gunkim.realworld.testcase.integration

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.IntegrationTest
import io.kotest.core.spec.DisplayName
import io.kotest.core.test.TestCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@DisplayName("Profiles Controller - Integration Test")
class ProfilesControllerIntegrationTest(
    @Autowired private val followUserService: FollowUserService,
) : IntegrationTest() {
    private lateinit var authenticatedUser: User
    private lateinit var profileUser: User
    private lateinit var token: String

    override suspend fun beforeEachTest(testCase: TestCase) {
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
                }.andDo { print() }
        }
        "POST /api/profiles/:username/follow - Follow a profile" {
            mockMvc.post(followEndpoint(profileUser.name)) {
                header(authHeaderName, token)
            }.andExpect {
                status { isOk() }
                jsonPath("$.profile.username") { value(profileUser.name) }
                jsonPath("$.profile.bio") { value(profileUser.bio) }
                jsonPath("$.profile.image") { value(profileUser.image) }
                jsonPath("$.profile.following") { value(true) }
            }.andDo { print() }
        }
        "DELETE /api/profiles/:username/follow - Unfollow a profile" {
            followUserService.followUser(authenticatedUser.id, profileUser.name)
            mockMvc.delete(followEndpoint(profileUser.name)) {
                header(authHeaderName, token)
            }.andExpect {
                status { isOk() }
                jsonPath("$.profile.username") { value(profileUser.name) }
                jsonPath("$.profile.bio") { value(profileUser.bio) }
                jsonPath("$.profile.image") { value(profileUser.image) }
                jsonPath("$.profile.following") { value(false) }
            }.andDo { print() }
        }
    }
}