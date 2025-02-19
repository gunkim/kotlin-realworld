package io.github.gunkim.realworld.web.api.user

import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.domain.user.service.GetUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.user.model.response.ProfileResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/profiles")
interface ProfilesResource {
    @GetMapping("/{username}")
    fun getProfile(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser?,
    ): ProfileResponse

    @PostMapping("/{username}/follow")
    fun follow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    )

    @DeleteMapping("/{username}/follow")
    fun unfollow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    )
}

@RestController
class ProfilesController(
    private val followUserService: FollowUserService,
    private val getUserService: GetUserService,
) : ProfilesResource {
    override fun getProfile(
        username: String,
        authenticatedUser: AuthenticatedUser?,
    ): ProfileResponse {
        val user = getUserService.getByUsername(username)
        val isFollowing = isUserFollowing(authenticatedUser, username)

        return ProfileResponse.of(user, isFollowing)
    }

    override fun follow(
        username: String,
        authenticatedUser: AuthenticatedUser,
    ) {
        followUserService.followUser(authenticatedUser.userId, username)
    }

    override fun unfollow(
        username: String,
        authenticatedUser: AuthenticatedUser,
    ) {
        followUserService.unfollowUser(authenticatedUser.userId, username)
    }

    private fun isUserFollowing(authenticatedUser: AuthenticatedUser?, targetUsername: String): Boolean {
        val userUuid = authenticatedUser?.userId ?: return false
        return followUserService.isFollowing(userUuid, targetUsername)
    }
}