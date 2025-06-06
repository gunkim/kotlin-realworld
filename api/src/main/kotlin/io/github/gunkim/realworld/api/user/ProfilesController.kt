package io.github.gunkim.realworld.api.user

import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.domain.user.service.GetUserService
import io.github.gunkim.realworld.api.AuthenticatedUser
import io.github.gunkim.realworld.api.user.model.response.ProfileResponse
import io.github.gunkim.realworld.api.user.model.response.wrapper.ProfileWrapper
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
    ): ProfileWrapper

    @PostMapping("/{username}/follow")
    fun follow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ) : ProfileWrapper

    @DeleteMapping("/{username}/follow")
    fun unfollow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ) : ProfileWrapper
}

@RestController
class ProfilesController(
    private val followUserService: FollowUserService,
    private val getUserService: GetUserService,
) : ProfilesResource {
    override fun getProfile(
        username: String,
        authenticatedUser: AuthenticatedUser?,
    ): ProfileWrapper {
        val user = getUserService.getByUsername(username)
        val isFollowing = isUserFollowing(authenticatedUser, username)

        return ProfileResponse.of(user, isFollowing)
            .let(::ProfileWrapper)
    }

    override fun follow(
        username: String,
        authenticatedUser: AuthenticatedUser,
    ): ProfileWrapper {
        val followee = followUserService.followUser(authenticatedUser.userId, username)
        val isFollowing = isUserFollowing(authenticatedUser, username)

        return ProfileResponse.of(followee, isFollowing)
            .let(::ProfileWrapper)
    }

    override fun unfollow(
        username: String,
        authenticatedUser: AuthenticatedUser,
    ): ProfileWrapper {
        val followee = followUserService.unfollowUser(authenticatedUser.userId, username)
        val isFollowing = isUserFollowing(authenticatedUser, username)

        return ProfileResponse.of(followee, isFollowing)
            .let(::ProfileWrapper)
    }

    private fun isUserFollowing(authenticatedUser: AuthenticatedUser?, targetUsername: String): Boolean {
        val userId = authenticatedUser?.userId ?: return false
        return followUserService.isFollowing(userId, targetUsername)
    }
}