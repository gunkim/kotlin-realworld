package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.UserFollowFindService
import io.github.gunkim.realworld.application.UserService
import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserFollow
import io.github.gunkim.realworld.domain.user.UserFollowService
import io.github.gunkim.realworld.domain.user.UserName
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.web.response.ProfileResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/profiles")
class ProfilesController(
    private val userService: UserService,
    private val userFollowService: UserFollowService,
    private val userFollowFindService: UserFollowFindService,
) {
    @GetMapping("/{username}")
    fun getProfile(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser?,
    ): ProfileResponse {
        val user = userService.findUserByName(UserName(username))
        val me: User? = authenticatedUser?.let { userService.findUserById(it.id) }

        val isFollowing = me?.let { userFollowFindService.isFollowing(me, user) } ?: false
        return ProfileResponse.of(user, isFollowing)
    }

    @PostMapping("/{username}/follow")
    fun follow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ) {
        val me = userService.findUserById(authenticatedUser.id)
        val user = userService.findUserByName(UserName(username))

        userFollowService.follow(UserFollow.of(me, user))
    }

    @DeleteMapping("/{username}/follow")
    fun unfollow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ) {
        val me = userService.findUserById(authenticatedUser.id)
        val user = userService.findUserByName(UserName(username))

        userFollowService.unfollow(me, user)
    }
}