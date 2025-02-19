package io.github.gunkim.realworld.domain.user.repository

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId

interface UserRepository : UserReadRepository {
    fun save(user: User): User
    fun follow(followerId: UserId, followeeId: UserId)
    fun unfollow(followerUuid: UserId, followeeUuid: UserId)
}