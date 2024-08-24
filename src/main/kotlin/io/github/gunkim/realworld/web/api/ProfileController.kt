package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.UserService
import io.github.gunkim.realworld.domain.user.UserName
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.web.response.ProfileResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/profiles")
class ProfileController(
    private val userService: UserService,
) {
    @GetMapping("/{username}")
    fun getProfile(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser?,
    ): ProfileResponse {
        val user = userService.findUserByName(UserName(username))

        val isFollowing = authenticatedUser?.let { user.isFollowedBy(it.id) } ?: false
        return ProfileResponse.of(user, isFollowing)
    }
}