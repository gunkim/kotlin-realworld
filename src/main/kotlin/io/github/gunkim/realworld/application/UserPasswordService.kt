package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.model.UserPasswordManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserPasswordService(
    private val passwordEncoder: BCryptPasswordEncoder,
) : UserPasswordManager {
    override fun matches(rawPassword: String, encodedPassword: String) =
        passwordEncoder.matches(rawPassword, encodedPassword)

    override fun encode(rawPassword: String) =
        passwordEncoder.encode(rawPassword)
}