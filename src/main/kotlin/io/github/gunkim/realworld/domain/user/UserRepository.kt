package io.github.gunkim.realworld.domain.user

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: Email): User?
    fun findById(id: UserId?): User?
    fun findByProfileName(name: UserName): User?
}