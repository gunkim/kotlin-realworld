package io.github.gunkim.realworld.infrastructure.jdbc.user.dao

import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UserDao : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?
    fun findByUuid(uuid: UUID): UserJpaEntity?
    fun findByName(username: String): UserJpaEntity?
}