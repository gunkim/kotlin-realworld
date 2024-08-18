package io.github.gunkim.realworld.domain.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional(readOnly = true)
    fun findUserByEmail(email: Email): User? {
        return userRepository.findByEmail(email)
    }

    fun authenticate(user: User, password: String) {
        require(passwordEncoder.matches(password, user.password)) { "Password does not match" }
    }
}