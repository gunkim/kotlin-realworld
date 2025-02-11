package io.github.gunkim.realworld.domain.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import java.util.UUID

interface UserReadRepository {
    fun findByEmail(email: String): User?
    fun findByUuid(uuid: UUID): User?
    fun findByUserName(name: String): User?
    fun existsFollowingIdAndFollowerUsername(uuid: UUID, targetUuid: UUID): Boolean
}