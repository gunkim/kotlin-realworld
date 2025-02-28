package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.auth.service.UserPasswordService
import io.github.gunkim.realworld.domain.user.exception.InvalidUserPasswordException
import io.github.gunkim.realworld.domain.user.model.User
import org.springframework.stereotype.Service

@Service
class AuthenticateUserService(
    val userPasswordService: UserPasswordService,
) {
    fun authenticate(user: User, rawPassword: String) {
        require(userPasswordService.matches(rawPassword, user.password)) {
            InvalidUserPasswordException("Password does not match")
        }
    }
}