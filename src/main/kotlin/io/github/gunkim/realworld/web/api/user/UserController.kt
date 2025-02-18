package io.github.gunkim.realworld.web.api.user

import io.github.gunkim.realworld.application.AuthenticationService
import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.service.GetUserService
import io.github.gunkim.realworld.domain.user.service.UpdateUserService
import io.github.gunkim.realworld.infrastructure.auth.JwtProvider
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.user.model.request.UserUpdateRequest
import io.github.gunkim.realworld.web.api.user.model.response.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/user")
interface UserResource {
    @GetMapping
    fun get(
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse

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
    private val authenticationService: AuthenticationService,
    private val updateUserService: UpdateUserService,
    private val getUserService: GetUserService,
    private val jwtProvider: JwtProvider,
) : UserResource {
    override fun get(authenticatedUser: AuthenticatedUser): UserResponse {
        val user = getUserService.getUserByUUID(authenticatedUser.uuid)
        return createUserResponse(user)
    }

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
        val token = authenticationService.createToken(updatedUser.uuid)
        return UserResponse.from(updatedUser, token)
    }

    private fun createUserResponse(user: User): UserResponse {
        val token = jwtProvider.create(user.uuid)
        return UserResponse.from(user, token)
    }
}
