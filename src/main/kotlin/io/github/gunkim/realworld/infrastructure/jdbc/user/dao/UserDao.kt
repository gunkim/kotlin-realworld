package io.github.gunkim.realworld.infrastructure.jdbc.user.dao

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserDao : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?
    fun findById(id: UserId): UserJpaEntity?
    fun findByName(username: String): UserJpaEntity?

    @Query(
        """
        SELECT u.id
        FROM users u
             INNER JOIN 
             user_follow f ON f.followeeUserDatabaseId = u.databaseId 
        WHERE u.id = :userId
    """
    )
    fun findAllByFollowerUserUuid(userId: UserId): List<UserId>
}