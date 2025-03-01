package io.github.gunkim.realworld.infrastructure.jpa.user.dao

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserDao : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun findById(id: UserId): UserEntity?
    fun findByName(username: String): UserEntity?

    @Query(
        """
        SELECT followee.id
        FROM users u
             INNER JOIN 
             user_follow f ON f.followerEntity = u
             INNER JOIN
             users followee ON f.followeeEntity = followee
        WHERE u.id = :userId
    """
    )
    fun findAllByFollowerUserId(userId: UserId): List<UserId>
}