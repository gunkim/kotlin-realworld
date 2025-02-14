package io.github.gunkim.realworld.infrastructure.auth

import io.jsonwebtoken.Jwts
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.UUID
import org.springframework.stereotype.Component
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider(
    private val secretKey: SecretKeySpec,
) {
    fun create(uuid: UUID): String {
        val now = LocalDateTime.now()
        val expirationTime = now.plusMinutes(EXPIRATION_MINUTES)

        return Jwts.builder()
            .signWith(secretKey)
            .claims()
            .add(USER_ID_PAYLOAD_PARAMETER, uuid)
            .and()
            .issuedAt(now.toDate())
            .expiration(expirationTime.toDate())
            .compact()
    }

    fun parse(jws: String): UUID = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(jws)
        .payload[USER_ID_PAYLOAD_PARAMETER]
        .toString()
        .let(UUID::fromString)

    companion object {
        private const val USER_ID_PAYLOAD_PARAMETER = "userId"
        private const val EXPIRATION_MINUTES = 30L
    }

    private fun LocalDateTime.toDate(): Date = Date.from(this.toInstant(ZoneOffset.UTC))
}