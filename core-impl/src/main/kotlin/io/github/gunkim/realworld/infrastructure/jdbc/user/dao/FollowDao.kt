package io.github.gunkim.realworld.infrastructure.jdbc.user.dao

import io.github.gunkim.realworld.infrastructure.jdbc.user.model.FollowJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FollowDao : JpaRepository<FollowJpaEntity, Long> {
    fun deleteByFolloweeUserDatabaseIdAndFollowerUserDatabaseId(followingDatabaseId: Int, followerDatabaseId: Int)
}