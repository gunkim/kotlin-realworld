package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.EncodedPassword
import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserId
import io.github.gunkim.realworld.domain.user.UserName
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
    fun findUserByEmail(email: Email): User = userRepository.findByEmail(email)
        ?: throw IllegalArgumentException("User not found")

    @Transactional
    fun registerUser(request: UserRegistrationRequest): User = request.run {
        require(userRepository.findByEmail(email) == null) { "User already exists" }

        val encodedPassword = EncodedPassword.of(password, passwordEncoder::encode)
        return userRepository.save(User.create(username, email, encodedPassword))
    }

    fun authenticate(user: User, rawPassword: String) {
        require(passwordEncoder.matches(rawPassword, user.password.value)) { "Password does not match" }
    }

    @Transactional(readOnly = true)
    fun findUserById(id: UserId): User = userRepository.findById(id)
        ?: throw IllegalArgumentException("User not found")

    @Transactional
    fun update(user: User) {
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun findUserByName(username: UserName) = userRepository.findByProfileName(username)
        ?: throw IllegalArgumentException("User not found")
}