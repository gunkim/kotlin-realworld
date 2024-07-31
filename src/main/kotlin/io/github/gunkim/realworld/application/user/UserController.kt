package io.github.gunkim.realworld.application.user

import io.github.gunkim.realworld.domain.user.UserRegistrationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRegistrationService: UserRegistrationService,
) {
    @PostMapping
    fun registration(
        @RequestBody
        request: UserRegistrationRequest,
    ): UserRegistrationResponse {
        val registeredUser = userRegistrationService.registerUser(request)
        return UserRegistrationResponse.from(registeredUser)
    }
}