package io.github.gunkim.realworld.application.usecase

import io.github.gunkim.realworld.application.JwtProvider
import io.github.gunkim.realworld.application.UserService
import io.github.gunkim.realworld.application.usecase.request.UserAuthenticateRequest
import io.github.gunkim.realworld.application.usecase.request.UserRegistrationRequest
import io.github.gunkim.realworld.application.usecase.request.UserUpdateRequest
import io.github.gunkim.realworld.application.usecase.response.UserResponse
import io.github.gunkim.realworld.domain.user.EncodedPassword
import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserUseCase(
    private val jwtProvider: JwtProvider,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) {
    fun update(
        authenticatedUser: AuthenticatedUser,
        request: UserUpdateRequest,
    ): UserResponse {
        val user = userService.findUserById(authenticatedUser.id)

        with(request) {
            val encodedPassword = password?.let { EncodedPassword.of(it, passwordEncoder::encode) }
            user.updateWhenNotNull(email, encodedPassword, username, image, bio)
            userService.update(user)
        }

        return createUserResponse(user)
    }

    @Transactional(readOnly = true)
    fun registration(request: UserRegistrationRequest): UserResponse {
        val registeredUser = userService.registerUser(request)
        return createUserResponse(registeredUser)
    }

    @Transactional(readOnly = true)
    fun authenticate(request: UserAuthenticateRequest): UserResponse {
        val user = userService.findUserByEmail(request.email)
        userService.authenticate(user, request.password)
        return createUserResponse(user)
    }

    @Transactional(readOnly = true)
    fun get(authenticatedUser: AuthenticatedUser): UserResponse {
        val user = userService.findUserById(authenticatedUser.id)
        return createUserResponse(user)
    }

    private fun createUserResponse(user: User): UserResponse {
        val token = jwtProvider.create(user.id!!)
        return UserResponse.from(user, token)
    }
}