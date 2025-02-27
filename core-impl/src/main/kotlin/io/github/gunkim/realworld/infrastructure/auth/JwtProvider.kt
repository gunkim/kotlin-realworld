package io.github.gunkim.realworld.infrastructure.auth

import io.github.gunkim.realworld.domain.auth.service.CreateTokenService
import io.github.gunkim.realworld.domain.user.model.UserId
import io.jsonwebtoken.Jwts
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.UUID
import org.springframework.stereotype.Service
import javax.crypto.spec.SecretKeySpec

@Service
class JwtProvider(
    private val secretKey: SecretKeySpec,
) : CreateTokenService {
    override fun createToken(userId: UserId): String {
        val now = LocalDateTime.now()
        val expirationTime = now.plusMinutes(EXPIRATION_MINUTES)

        return Jwts.builder()
            .signWith(secretKey)
            .claims()
            .add(USER_ID_PAYLOAD_PARAMETER, userId.toString())
            .and()
            .issuedAt(now.toDate())
            .expiration(expirationTime.toDate())
            .compact()
    }

    override fun parse(jws: String): UserId = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(jws)
        .payload[USER_ID_PAYLOAD_PARAMETER]
        .toString()
        .let(UUID::fromString)
        .let(UserId.Companion::from)

    companion object {
        private const val USER_ID_PAYLOAD_PARAMETER = "userId"
        private const val EXPIRATION_MINUTES = 30L
    }

    private fun LocalDateTime.toDate(): Date = Date.from(this.toInstant(ZoneOffset.UTC))
}