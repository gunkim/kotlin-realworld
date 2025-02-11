package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.infrastructure.auth.JwtProvider
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val jwtProvider: JwtProvider
) {
    fun createToken(uuid: UUID): String {
        return jwtProvider.create(uuid)
    }
}