package io.github.gunkim.realworld.infrastructure.jpa.user.dao

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserDao : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?
    fun findById(id: UserId): UserJpaEntity?
    fun findByName(username: String): UserJpaEntity?

    @Query(
        """
        SELECT followee.id
        FROM users u
             INNER JOIN 
             user_follow f ON f.followerUserDatabaseId = u.databaseId
             INNER JOIN
             users followee ON f.followeeUserDatabaseId = followee.databaseId
        WHERE u.id = :userId
    """
    )
    fun findAllByFollowerUserId(userId: UserId): List<UserId>
}