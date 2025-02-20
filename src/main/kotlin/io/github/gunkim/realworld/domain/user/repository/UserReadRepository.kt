package io.github.gunkim.realworld.domain.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId

interface UserReadRepository {
    fun findByEmail(email: String): User?
    fun findById(userId: UserId): User?
    fun findByUserName(name: String): User?
    fun existsFollowingIdAndFollowerUsername(followerId: UserId, followeeId: UserId): Boolean
    fun findFollowingUserUuids(userId: UserId): List<UserId>
}