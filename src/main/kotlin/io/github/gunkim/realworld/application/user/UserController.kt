package io.github.gunkim.realworld.application.user

import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.UserRegistrationService
import io.github.gunkim.realworld.domain.user.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRegistrationService: UserRegistrationService,
    private val userService: UserService,
    private val jwtProvider: JwtProvider,
) {
    @PostMapping
    fun registration(
        @RequestBody
        request: UserRegistrationRequest,
    ): UserRegistrationResponse {
        val registeredUser = userRegistrationService.registerUser(request)
        return UserRegistrationResponse.from(registeredUser)
    }

    @PostMapping("/login")
    fun authenticate(
        @RequestBody
        request: UserAuthenticateRequest,
    ): UserResponse {
        val user = userService.findUserByEmail(Email(request.email))
            ?: throw IllegalArgumentException("User not found")

        userService.authenticate(user, request.password)

        return UserResponse(
            user.email.value,
            jwtProvider.create(user.id!!),
            user.profile.name.value,
            user.profile.bio,
            user.profile.image
        )
    }
}