package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.application.JwtProvider
import io.github.gunkim.realworld.application.UserService
import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.EncodedPassword
import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.web.request.UserUpdateRequest
import io.github.gunkim.realworld.web.response.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserUpdateController(
    private val userService: UserService,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
) {
    @PutMapping
    fun update(
        @JsonRequest("user")
        request: UserUpdateRequest,
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse {
        val user = userService.findUserById(authenticatedUser.id)

        request.apply {
            val encodedPassword = password?.let { EncodedPassword.of(it, passwordEncoder::encode) }
            user.updateWhenNotNull(email, encodedPassword, username, image, bio)

            userService.update(user)
        }

        return createUserResponse(user)
    }

    private fun createUserResponse(user: User): UserResponse {
        val token = jwtProvider.create(user.id!!)
        return UserResponse.from(user, token)
    }
}