package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.domain.user.UserName
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.application.usecase.response.ProfileResponse
import io.github.gunkim.realworld.application.usecase.ProfileUseCase
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
    private val profileUseCase: ProfileUseCase,
) {
    @GetMapping("/{username}")
    fun getProfile(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser?,
    ): ProfileResponse = profileUseCase.get(authenticatedUser, UserName(username))

    @PostMapping("/{username}/follow")
    fun follow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ) {
        profileUseCase.follow(authenticatedUser, UserName(username))
    }

    @DeleteMapping("/{username}/follow")
    fun unfollow(
        @PathVariable username: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ) {
        profileUseCase.unfollow(authenticatedUser, UserName(username))
    }
}