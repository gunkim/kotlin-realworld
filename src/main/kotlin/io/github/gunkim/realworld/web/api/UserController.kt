package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.application.usecase.request.UserUpdateRequest
import io.github.gunkim.realworld.application.usecase.response.UserResponse
import io.github.gunkim.realworld.application.usecase.UserUseCase
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val useCase: UserUseCase,
) {
    @PutMapping
    fun update(
        @JsonRequest("user")
        request: UserUpdateRequest,
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse = useCase.update(authenticatedUser, request)
}