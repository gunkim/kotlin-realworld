package io.github.gunkim.realworld.infrastructure.jdbc.user.dao

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserDao : JpaRepository<io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity, Long> {
    fun findByEmail(email: String): io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity?
    fun findById(id: UserId): io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity?
    fun findByName(username: String): io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity?

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