package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.JwtProvider
import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.service.UpdateUserService
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.web.model.request.UserUpdateRequest
import io.github.gunkim.realworld.web.model.response.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/user")
interface UserResource {
    @PutMapping
    fun update(
        @JsonRequest("user")
        request: UserUpdateRequest,
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse
}

@RestController
class UserController(
    private val jwtProvider: JwtProvider,
    private val updateUserService: UpdateUserService,
) : UserResource {
    override fun update(
        request: UserUpdateRequest,
        authenticatedUser: AuthenticatedUser,
    ): UserResponse {
        val updatedUser = updateUserService.updateUser(
            authenticatedUser.uuid,
            request.email,
            request.username,
            request.image,
            request.password,
            request.bio
        )
        val token = jwtProvider.create(updatedUser.uuid)
        return UserResponse.from(updatedUser, token)
    }
}