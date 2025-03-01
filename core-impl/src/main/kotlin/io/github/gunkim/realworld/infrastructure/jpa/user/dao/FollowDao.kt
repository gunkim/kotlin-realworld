package io.github.gunkim.realworld.infrastructure.jpa.user.dao

import io.github.gunkim.realworld.infrastructure.jpa.user.model.FollowEntity
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FollowDao : JpaRepository<FollowEntity, Long> {
    fun deleteByFolloweeEntityAndFollowerEntity(followeeUserEntity: UserEntity, followerUserEntity: UserEntity)
}