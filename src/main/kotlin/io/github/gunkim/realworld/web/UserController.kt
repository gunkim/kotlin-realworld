package io.github.gunkim.realworld.web

import io.github.gunkim.realworld.application.UserRegistrationRequest
import io.github.gunkim.realworld.application.UserRegistrationResponse
import io.github.gunkim.realworld.application.UserRegistrationService
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
    ): UserBaseResponse<UserRegistrationResponse> {
        val response = userRegistrationService.registerUser(request)
        return UserBaseResponse(response)
    }
}

data class UserBaseResponse<T>(
    val user: T,
)