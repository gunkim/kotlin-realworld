package io.github.gunkim.realworld.domain.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import java.util.UUID

interface UserRepository : UserReadRepository {
    fun save(user: User): User
    fun follow(uuid: UUID, targetUuid: UUID)
    fun unfollow(uuid: UUID, targetUuid: UUID)
}