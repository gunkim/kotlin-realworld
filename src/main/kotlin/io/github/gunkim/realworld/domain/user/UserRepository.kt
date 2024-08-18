package io.github.gunkim.realworld.domain.user

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: Email): User?
}