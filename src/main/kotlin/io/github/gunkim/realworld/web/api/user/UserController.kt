package io.github.gunkim.realworld.web.api.user

import io.github.gunkim.realworld.application.AuthenticationService
import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.service.UpdateUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.user.model.request.UserUpdateRequest
import io.github.gunkim.realworld.web.api.user.model.response.UserResponse
import java.util.UUID
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
    private val authenticationService: AuthenticationService,
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
        val token = authenticationService.createToken(updatedUser.uuid)
        return UserResponse.from(updatedUser, token)
    }
}
