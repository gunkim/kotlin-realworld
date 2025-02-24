package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.domain.user.model.UserId

data class AuthenticatedUser(
    val userId: UserId,
)