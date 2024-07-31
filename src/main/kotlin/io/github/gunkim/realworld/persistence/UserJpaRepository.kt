package io.github.gunkim.realworld.persistence

import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserId
import io.github.gunkim.realworld.domain.user.UserRepository
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, UserId>, UserRepository {
}