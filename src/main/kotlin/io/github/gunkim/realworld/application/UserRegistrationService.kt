package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserRepository
import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.UserName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRegistrationService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun registerUser(
        username: UserName,
        email: Email,
        password: String,
    ) {
        userRepository.save(User.create(username, email, password))
    }
}