package io.github.gunkim.realworld.web.api.user

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.infrastructure.auth.JwtProvider
import io.github.gunkim.realworld.web.api.user.model.response.UserResponse
import org.springframework.stereotype.Component

@Component
class UserResponseAssembler(
    private val jwtProvider: JwtProvider,
) {
    fun assembleUserResponse(user: User): UserResponse {
        val token = jwtProvider.create(user.uuid)
        return UserResponse.from(user, token)
    }
}