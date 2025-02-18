package io.github.gunkim.realworld.web.api.user

import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.github.gunkim.realworld.domain.user.service.GetUserService
import io.github.gunkim.realworld.web.api.user.model.request.UserAuthenticateRequest
import io.github.gunkim.realworld.web.api.user.model.request.UserRegistrationRequest
import io.github.gunkim.realworld.web.api.user.model.response.UserResponse
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
}

@RestController
class UsersController(
    private val authenticateUserService: AuthenticateUserService,
    private val userResponseAssembler: UserResponseAssembler,
    private val createUserService: CreateUserService,
    private val getUserService: GetUserService,
) : UsersResource {
    override fun registration(request: UserRegistrationRequest): UserResponse {
        val registeredUser = createUserService.createUser(
            email = request.email,
            username = request.username,
            encodedPassword = authenticateUserService.encodePassword(request.password),
        )
        return userResponseAssembler.assembleUserResponse(registeredUser)
    }

    override fun authenticate(request: UserAuthenticateRequest): UserResponse {
        val user = getUserService.getByEmail(request.email)
        authenticateUserService.authenticate(user, request.password)
        return userResponseAssembler.assembleUserResponse(user)
    }
}
