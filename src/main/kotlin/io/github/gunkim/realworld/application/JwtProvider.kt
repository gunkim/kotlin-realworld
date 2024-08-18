package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.UserId
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Component
class JwtProvider {
    /**
     * Creates a JWT token for the given user ID.
     *
     * @param userId the ID of the user for whom the JWT token is being created
     * @return the generated JWT token as a string
     */
    fun create(userId: UserId): String {
        val now = LocalDateTime.now()
        val expirationTime = now.plusMinutes(EXPIRATION_MINUTES)

        return Jwts.builder()
            .signWith(SECRET_KEY)
            .header()
            .add("typ", "JWT")
            .add("alg", "HS256")
            .and()
            .claims()
            .add(USER_ID_PAYLOAD_PARAMETER, userId.value)
            .and()
            .issuer(ISSUER)
            .issuedAt(Date.from(now.toInstant(ZoneOffset.UTC)))
            .expiration(Date.from(expirationTime.toInstant(ZoneOffset.UTC)))
            .compact()
    }

    /**
     * Parses a JSON Web Signature (JWS) and retrieves the user ID from its payload.
     *
     * @param jws the JWS string to parse
     * @return the user ID extracted from the JWS payload as a Long
     */
    fun parse(jws: String) = Jwts.parser()
        .verifyWith(SECRET_KEY)
        .build()
        .parseSignedClaims(jws)
        .payload[USER_ID_PAYLOAD_PARAMETER].toString().toLong()

    companion object {
        //TODO: It seems better to separate all the key signature information here as properties.
        private const val SECRET_KEY_STRING = "s7tT5+Z/jfY47K3JqKDl8xhAyqTDynkxNoB/qBcIZd8="
        private val SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_STRING))
        private const val ISSUER = "짱구"
        private const val USER_ID_PAYLOAD_PARAMETER = "userId"

        private const val EXPIRATION_MINUTES = 30L
    }
}