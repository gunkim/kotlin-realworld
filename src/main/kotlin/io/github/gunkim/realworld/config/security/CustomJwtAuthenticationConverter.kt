package io.github.gunkim.realworld.config.security

import io.github.gunkim.realworld.domain.user.UserId
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import java.util.*

class CustomJwtAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val id = UserId(
            UUID.fromString(
                jwt.claims["userId"] as String?
                    ?: throw IllegalArgumentException("userId is required")
            )
        )

        val authenticatedUser = AuthenticatedUser(id = id)
        val authorities = JwtAuthenticationConverter().convert(jwt)?.authorities ?: emptyList()

        return CustomJwtAuthenticationToken(authenticatedUser, authorities)
    }
}