package io.github.gunkim.realworld.config.security

import io.github.gunkim.realworld.api.AuthenticatedUser
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority


class CustomJwtAuthenticationToken(
    private val authenticatedUser: AuthenticatedUser,
    authorities: Collection<GrantedAuthority>,
) : AbstractAuthenticationToken(authorities) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any = authenticatedUser

    init {
        super.setAuthenticated(true)
    }
}