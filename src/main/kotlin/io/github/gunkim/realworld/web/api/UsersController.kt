package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.application.usecase.request.UserAuthenticateRequest
import io.github.gunkim.realworld.application.usecase.request.UserRegistrationRequest
import io.github.gunkim.realworld.application.usecase.response.UserResponse
import io.github.gunkim.realworld.application.usecase.UserUseCase
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val useCase: UserUseCase,
) {
    @PostMapping
    fun registration(
        @JsonRequest("user")
        request: UserRegistrationRequest,
    ): UserResponse = useCase.registration(request)

    @PostMapping("/login")
    fun authenticate(
        @JsonRequest("user")
        request: UserAuthenticateRequest,
    ): UserResponse = useCase.authenticate(request)

    @GetMapping
    fun get(
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse = useCase.get(authenticatedUser)
}