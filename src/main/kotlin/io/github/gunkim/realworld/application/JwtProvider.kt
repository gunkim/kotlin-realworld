package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.config.SymmetricKeyProvider
import io.jsonwebtoken.Jwts
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class JwtProvider(
    private val symmetricKey: SymmetricKeyProvider,
) {
    fun create(uuid: UUID): String {
        val now = LocalDateTime.now()
        val expirationTime = now.plusMinutes(EXPIRATION_MINUTES)

        return Jwts.builder()
            .signWith(symmetricKey.key)
            .claims()
            .add(USER_ID_PAYLOAD_PARAMETER, uuid)
            .and()
            .issuedAt(now.toDate())
            .expiration(expirationTime.toDate())
            .compact()
    }

    fun parse(jws: String): UUID = Jwts.parser()
        .verifyWith(symmetricKey.key)
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