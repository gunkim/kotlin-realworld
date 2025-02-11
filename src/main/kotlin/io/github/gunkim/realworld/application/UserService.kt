package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val createUserService: CreateUserService,
) {
    fun registerUser(
        username: String,
        email: String,
        password: String,
    ): User {
        require(userRepository.findByEmail(email) == null) { "User already exists" }

        return createUserService.createUser(email, username, password)
    }

    @Transactional(readOnly = true)
    fun findUserByEmail(email: String): User = userRepository.findByEmail(email)
        ?: throw UserNotFoundException.fromEmail(email)

    @Transactional(readOnly = true)
    fun findUserById(uuid: UUID): User = userRepository.findByUuid(uuid)
        ?: throw UserNotFoundException.fromUUID(uuid)
}