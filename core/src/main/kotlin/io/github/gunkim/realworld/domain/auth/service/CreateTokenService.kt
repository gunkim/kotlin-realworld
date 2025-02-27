package io.github.gunkim.realworld.domain.auth.service

import io.github.gunkim.realworld.domain.user.model.UserId

interface CreateTokenService {
    fun createToken(userId: UserId): String
    fun parse(jws: String): UserId
}