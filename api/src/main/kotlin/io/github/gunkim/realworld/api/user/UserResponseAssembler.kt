package io.github.gunkim.realworld.api.user

import io.github.gunkim.realworld.domain.auth.service.CreateTokenService
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.api.user.model.response.UserResponse
import org.springframework.stereotype.Component

@Component
class UserResponseAssembler(
    private val createTokenService: CreateTokenService,
) {
    fun assembleUserResponse(user: User): UserResponse {
        val token = createTokenService.createToken(user.id)
        return UserResponse.from(user, token)
    }
}