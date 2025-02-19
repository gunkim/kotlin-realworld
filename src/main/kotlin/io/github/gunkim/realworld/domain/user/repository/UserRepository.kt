package io.github.gunkim.realworld.domain.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import java.util.UUID

interface UserRepository : UserReadRepository {
    fun save(user: User): User
    fun follow(followerUuid: UUID, followeeUuid: UUID)
    fun unfollow(followerUuid: UUID, followeeUuid: UUID)
}