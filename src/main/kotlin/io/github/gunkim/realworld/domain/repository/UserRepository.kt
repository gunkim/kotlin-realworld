package io.github.gunkim.realworld.domain.repository

import io.github.gunkim.realworld.domain.entity.User
import io.github.gunkim.realworld.domain.vo.UserId
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, UserId>