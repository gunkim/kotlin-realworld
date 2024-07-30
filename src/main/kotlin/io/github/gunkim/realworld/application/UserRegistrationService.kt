package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.entity.User
import io.github.gunkim.realworld.domain.repository.UserRepository
import io.github.gunkim.realworld.domain.vo.Email
import io.github.gunkim.realworld.domain.vo.UserName
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