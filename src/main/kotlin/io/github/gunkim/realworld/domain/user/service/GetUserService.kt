package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.UserFindable
import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class GetUserService(
    override val userRepository: UserRepository,
) : UserFindable {
    fun getByEmail(email: String): User = userRepository.findByEmail(email)
        ?: throw UserNotFoundException.fromEmail(email)

    fun getByUsername(username: String): User = userRepository.findByUserName(username)
        ?: throw UserNotFoundException.fromUserName(username)
}