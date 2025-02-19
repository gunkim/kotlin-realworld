package io.github.gunkim.realworld.config.security

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.share.AuthenticatedUser
import java.util.UUID
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

class CustomJwtAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val id = UUID.fromString(
            jwt.claims["userId"] as String?
                ?: throw IllegalArgumentException("userId is required")
        )

        val authenticatedUser = AuthenticatedUser(userId = UserId(id))
        val authorities = JwtAuthenticationConverter().convert(jwt)?.authorities ?: emptyList()

        return CustomJwtAuthenticationToken(authenticatedUser, authorities)
    }
}