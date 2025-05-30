package io.github.gunkim.realworld.infrastructure.auth

import io.github.gunkim.realworld.domain.auth.service.UserPasswordService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserPasswordService(
    private val passwordEncoder: BCryptPasswordEncoder,
) : UserPasswordService {
    override fun matches(rawPassword: String, encodedPassword: String) =
        passwordEncoder.matches(rawPassword, encodedPassword)

    override fun encode(rawPassword: String) =
        passwordEncoder.encode(rawPassword)
}