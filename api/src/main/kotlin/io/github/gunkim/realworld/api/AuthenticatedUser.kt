package io.github.gunkim.realworld.api

import io.github.gunkim.realworld.domain.user.model.UserId

data class AuthenticatedUser(
    val userId: UserId,
)