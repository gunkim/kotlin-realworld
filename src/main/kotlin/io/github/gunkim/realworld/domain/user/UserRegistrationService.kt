package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.application.user.UserRegistrationRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRegistrationService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun registerUser(request: UserRegistrationRequest): User = request.run {
        return userRepository.save(User.create(username, email, password))
    }
}