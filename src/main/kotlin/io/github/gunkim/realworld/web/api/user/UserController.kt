package io.github.gunkim.realworld.web.api.user

import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.service.GetUserService
import io.github.gunkim.realworld.domain.user.service.UpdateUserService
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
    private val userResponseAssembler: UserResponseAssembler,
    private val updateUserService: UpdateUserService,
    private val getUserService: GetUserService,
) : UserResource {
    override fun get(authenticatedUser: AuthenticatedUser): UserResponse {
        val user = getUserService.getUserByUUID(authenticatedUser.uuid)
        return userResponseAssembler.assembleUserResponse(user)
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
        return userResponseAssembler.assembleUserResponse(updatedUser)
    }
}
