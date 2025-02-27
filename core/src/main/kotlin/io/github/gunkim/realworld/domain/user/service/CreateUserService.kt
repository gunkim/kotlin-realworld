package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CreateUserService(
    private val userRepository: UserRepository,
) {
    fun createUser(
        email: String,
        username: String,
        encodedPassword: String,
    ) = User.create(
        email = email,
        password = encodedPassword,
        name = username
    ).let(userRepository::save)
}