package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.UserId
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Component
class JwtProvider(
    @Value("\${jwt.secret.private}")
    private val rsaPrivateKey: RSAPrivateKey,
    @Value("\${jwt.secret.public}")
    private val rsaPublicKey: RSAPublicKey,
) {
    /**
     * Creates a JSON Web Token (JWT) with the specified user ID.
     *
     * @param userId the ID of the user
     * @return the JWT string
     */
    fun create(userId: UserId): String {
        val now = LocalDateTime.now()
        val expirationTime = now.plusMinutes(EXPIRATION_MINUTES)

        return Jwts.builder()
            .signWith(rsaPrivateKey)
            .header()
            .add("typ", "JWT")
            .add("alg", "RS256")
            .and()
            .claims()
            .add(USER_ID_PAYLOAD_PARAMETER, userId.value)
            .and()
            .issuedAt(Date.from(now.toInstant(ZoneOffset.UTC)))
            .expiration(Date.from(expirationTime.toInstant(ZoneOffset.UTC)))
            .compact()
    }

    /**
     * Parses a JSON Web Token (JWT) and extracts the user ID from the payload.
     *
     * @param jws the JWT string to parse
     * @return the user ID extracted from the payload
     */
    fun parse(jws: String) = Jwts.parser()
        .verifyWith(rsaPublicKey)
        .build()
        .parseSignedClaims(jws)
        .payload[USER_ID_PAYLOAD_PARAMETER].toString().toLong()

    companion object {
        private const val USER_ID_PAYLOAD_PARAMETER = "userId"
        private const val EXPIRATION_MINUTES = 30L
    }
}