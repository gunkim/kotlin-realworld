package io.github.gunkim.realworld.infrastructure.jdbc.user.dao

import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserDao : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?
    fun findByUuid(uuid: UUID): UserJpaEntity?
    fun findByName(username: String): UserJpaEntity?

    @Query(
        """
        SELECT u.uuid
        FROM users u
             INNER JOIN 
             user_follow f ON f.followeeId = u.userId 
        WHERE u.uuid = :userUuid
    """
    )
    fun findAllByFollowerUserUuid(userUuid: UUID): List<UUID>
}