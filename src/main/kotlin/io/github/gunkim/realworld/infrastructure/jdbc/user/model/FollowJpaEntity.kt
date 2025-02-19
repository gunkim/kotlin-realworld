package io.github.gunkim.realworld.infrastructure.jdbc.user.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "user_follow")
class FollowJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val followId: Int?,
    val followeeId: Int,
    val followerId: Int,
    val createdAt: Instant,
    val updatedAt: Instant = Instant.now(),
) {
    companion object {
        fun of(followeeId: Int, followerId: Int) =
            FollowJpaEntity(
                followId = null,
                followeeId = followeeId,
                followerId = followerId,
                createdAt = Instant.now()
            )
    }
}