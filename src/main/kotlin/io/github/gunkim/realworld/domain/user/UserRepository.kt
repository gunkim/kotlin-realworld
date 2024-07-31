package io.github.gunkim.realworld.domain.user

interface UserRepository {
    fun save(user: User): User
}