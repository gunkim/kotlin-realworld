package io.github.gunkim.realworld.web.api.user

import io.github.gunkim.realworld.infrastructure.auth.JwtProvider
import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.github.gunkim.realworld.domain.user.service.GetUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.user.model.request.UserAuthenticateRequest
import io.github.gunkim.realworld.web.api.user.model.request.UserRegistrationRequest
import io.github.gunkim.realworld.web.api.user.model.response.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
interface UsersResource {
    @PostMapping
    fun registration(
        @JsonRequest("user")
        request: UserRegistrationRequest,
    ): UserResponse

    @PostMapping("/login")
    fun authenticate(
        @JsonRequest("user")
        request: UserAuthenticateRequest,
    ): UserResponse

    @GetMapping
    fun get(
        @AuthenticationPrincipal
        authenticatedUser: AuthenticatedUser,
    ): UserResponse
}

@RestController
class UsersController(
    private val jwtProvider: JwtProvider,
    private val getUserService: GetUserService,
    private val createUserService: CreateUserService,
    private val authenticateUserService: AuthenticateUserService,
) : UsersResource {
    override fun registration(request: UserRegistrationRequest): UserResponse {
        val registeredUser = createUserService.createUser(
            email = request.email,
            username = request.username,
            encodedPassword = authenticateUserService.encodePassword(request.password),
        )
        return createUserResponse(registeredUser)
    }

    override fun authenticate(request: UserAuthenticateRequest): UserResponse {
        val user = getUserService.getByEmail(request.email)
        authenticateUserService.authenticate(user, request.password)
        return createUserResponse(user)
    }

    override fun get(authenticatedUser: AuthenticatedUser): UserResponse {
        val user = getUserService.getUserByUUID(authenticatedUser.uuid)
        return createUserResponse(user)
    }

    private fun createUserResponse(user: User): UserResponse {
        val token = jwtProvider.create(user.uuid)
        return UserResponse.from(user, token)
    }
}
