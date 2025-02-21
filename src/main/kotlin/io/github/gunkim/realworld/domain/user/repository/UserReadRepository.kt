package io.github.gunkim.realworld.domain.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId

interface UserReadRepository {
    fun findByEmail(email: String): User?
    fun findById(userId: UserId): User?
    fun findByUserName(name: String): User?
    fun existsFollowerIdAndFolloweeId(followerId: UserId, followeeId: UserId): Boolean
    fun findFollowedUserIdsFor(userId: UserId): List<UserId>
}