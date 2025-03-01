package io.github.gunkim.realworld.infrastructure.jpa.user.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "user_follow")
class FollowEntity(
    @Id
    @Column(name = "follow_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "followee_id")
    val followeeEntity: UserEntity,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id")
    val followerEntity: UserEntity,
    val createdAt: Instant,
    val updatedAt: Instant = Instant.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FollowEntity

        if (databaseId != other.databaseId) return false
        if (followeeEntity != other.followeeEntity) return false
        if (followerEntity != other.followerEntity) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + followeeEntity.hashCode()
        result = 31 * result + followerEntity.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

    companion object {
        fun of(followeeUserEntity: UserEntity, followerUserEntity: UserEntity) =
            FollowEntity(
                databaseId = null,
                followeeEntity = followeeUserEntity,
                followerEntity = followerUserEntity,
                createdAt = Instant.now()
            )
    }
}