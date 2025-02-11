package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.user.exception.InvalidUserPasswordException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserPasswordManager
import org.springframework.stereotype.Service

@Service
class AuthenticateUserService(
    val userPasswordManager: UserPasswordManager,
) {
    fun authenticate(user: User, rawPassword: String) {
        require(userPasswordManager.matches(rawPassword, user.password)) {
            InvalidUserPasswordException("Password does not match")
        }
    }

    fun encodePassword(rawPassword: String): String = userPasswordManager.encode(rawPassword)
}