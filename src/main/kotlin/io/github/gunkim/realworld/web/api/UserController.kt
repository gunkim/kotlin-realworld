package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.JwtProvider
import io.github.gunkim.realworld.application.UserService
import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.web.request.UserAuthenticateRequest
import io.github.gunkim.realworld.web.request.UserRegistrationRequest
import io.github.gunkim.realworld.web.response.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtProvider: JwtProvider,
) {
    @PostMapping
    fun registration(
        @JsonRequest("user")
        request: UserRegistrationRequest,
    ): UserResponse {
        val registeredUser = userService.registerUser(request)
        return createUserResponse(registeredUser)
    }

    @PostMapping("/login")
    fun authenticate(
        @JsonRequest("user")
        request: UserAuthenticateRequest,
    ): UserResponse {
        val user = userService.findUserByEmail(request.email)
        userService.authenticate(user, request.password)
        return createUserResponse(user)
    }

    @GetMapping
    fun get(
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse {
        val user = userService.findUserById(authenticatedUser.id)
        return createUserResponse(user)
    }

    private fun createUserResponse(user: User): UserResponse {
        val token = jwtProvider.create(user.id!!)
        return UserResponse.from(user, token)
    }
}