package io.github.gunkim.realworld.infrastructure.jdbc.user.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "user_follow")
class FollowJpaEntity(
    @Id
    @Column(name = "follow_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val followDatabaseId: Int?,
    @Column(name = "followee_id")
    val followeeUserDatabaseId: Int,
    @Column(name = "follower_id")
    val followerUserDatabaseId: Int,
    val createdAt: Instant,
    val updatedAt: Instant = Instant.now(),
) {
    companion object {
        fun of(followeeId: Int, followerId: Int) =
            FollowJpaEntity(
                followDatabaseId = null,
                followeeUserDatabaseId = followeeId,
                followerUserDatabaseId = followerId,
                createdAt = Instant.now()
            )
    }
}