package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.application.user.UserRegistrationRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRegistrationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registerUser(request: UserRegistrationRequest): User = request.run {
        require(userRepository.findByEmail(email) == null) { "User already exists" }

        val encodedPassword = passwordEncoder.encode(password)
        return userRepository.save(User.create(username, email, encodedPassword))
    }
}