package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserRepository
import io.github.gunkim.realworld.web.request.UserRegistrationRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional(readOnly = true)
    fun findUserByEmail(email: Email): User {
        return userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")
    }

    @Transactional
    fun registerUser(request: UserRegistrationRequest): User = request.run {
        require(userRepository.findByEmail(email) == null) { "User already exists" }

        val encodedPassword = passwordEncoder.encode(password)
        return userRepository.save(User.create(username, email, encodedPassword))
    }

    fun authenticate(user: User, password: String) {
        require(passwordEncoder.matches(password, user.password)) { "Password does not match" }
    }
}