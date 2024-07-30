package io.github.gunkim.realworld.web

import io.github.gunkim.realworld.application.UserRegistrationService
import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.UserName
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
    ) {
        request.user.apply { run() }
    }

    private fun UserRegistrationRequest.UserDetailRequest.run() {
        userRegistrationService.registerUser(
            UserName(username),
            Email(email),
            password
        )
    }
}

data class UserRegistrationRequest(
    val user: UserDetailRequest,
) {
    data class UserDetailRequest(
        val username: String,
        val email: String,
        val password: String,
    )
}