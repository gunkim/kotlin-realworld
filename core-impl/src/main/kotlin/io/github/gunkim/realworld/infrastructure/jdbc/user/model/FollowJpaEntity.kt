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
    val databaseId: Int?,
    @Column(name = "followee_id")
    val followeeUserDatabaseId: Int,
    @Column(name = "follower_id")
    val followerUserDatabaseId: Int,
    val createdAt: Instant,
    val updatedAt: Instant = Instant.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FollowJpaEntity

        if (databaseId != other.databaseId) return false
        if (followeeUserDatabaseId != other.followeeUserDatabaseId) return false
        if (followerUserDatabaseId != other.followerUserDatabaseId) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + followeeUserDatabaseId
        result = 31 * result + followerUserDatabaseId
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

    companion object {
        fun of(followeeId: Int, followerId: Int) =
            FollowJpaEntity(
                databaseId = null,
                followeeUserDatabaseId = followeeId,
                followerUserDatabaseId = followerId,
                createdAt = Instant.now()
            )
    }
}