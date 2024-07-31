package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRegistrationService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun registerUser(request: UserRegistrationRequest): UserRegistrationResponse = request.run {
        val savedUser = userRepository.save(User.create(username, email, password))

        return UserRegistrationResponse(
            savedUser.profile.name.value,
            savedUser.email.value,
            savedUser.password
        )
    }
}