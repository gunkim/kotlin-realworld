package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.auth.service.UserPasswordService
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CreateUserService(
    private val userRepository: UserRepository,
    private val userPasswordService: UserPasswordService,
) {
    fun createUser(
        email: String,
        username: String,
        password: String,
    ) = User.create(
        email = email,
        password = userPasswordService.encode(password),
        name = username
    ).let(userRepository::save)
}