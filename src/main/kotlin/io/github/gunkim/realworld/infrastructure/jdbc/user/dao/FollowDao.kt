package io.github.gunkim.realworld.infrastructure.jdbc.user.dao

import io.github.gunkim.realworld.infrastructure.jdbc.user.model.FollowJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FollowDao : JpaRepository<FollowJpaEntity, Long> {
    fun existsByFolloweeIdAndFollowerId(followingId: Int, followerId: Int): Boolean
    fun deleteByFolloweeIdAndFollowerId(followingId: Int, followerId: Int)
}