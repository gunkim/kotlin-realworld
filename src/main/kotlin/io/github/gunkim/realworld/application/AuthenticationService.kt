package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.auth.JwtProvider
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val jwtProvider: JwtProvider
) {
    fun createToken(userId: UserId): String {
        return jwtProvider.create(userId)
    }
}