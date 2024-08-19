package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.JwtProvider
import io.github.gunkim.realworld.application.UserService
import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.web.request.UserAuthenticateRequest
import io.github.gunkim.realworld.web.request.UserRegistrationRequest
import io.github.gunkim.realworld.web.response.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtProvider: JwtProvider,
) {
    @PostMapping
    fun registration(
        @RequestBody
        request: UserRegistrationRequest,
    ): UserResponse {
        val registeredUser = userService.registerUser(request)
        return UserResponse.from(registeredUser, jwtProvider.create(registeredUser.id!!))
    }

    @PostMapping("/login")
    fun authenticate(
        @RequestBody
        request: UserAuthenticateRequest,
    ): UserResponse {
        val user = userService.findUserByEmail(Email(request.email))

        userService.authenticate(user, request.password)

        return UserResponse.from(user, jwtProvider.create(user.id!!))
    }

    @GetMapping
    fun get(
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse {
        val user = userService.findUserById(authenticatedUser.id)
        return UserResponse.from(user, jwtProvider.create(user.id!!))
    }
}