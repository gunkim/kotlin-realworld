package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.user.UserFindable
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.model.UserPasswordManager
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UpdateUserService(
    override val userRepository: UserRepository,
    private val passwordManager: UserPasswordManager,
) : UserFindable {
    fun updateUser(
        authorId: UserId,
        email: String?,
        username: String?,
        image: String?,
        password: String?,
        bio: String?,
    ): User {
        val user = super.getUserById(authorId)

        return userRepository.save(user.edit().apply {
            this.email = email ?: user.email
            this.name = username ?: user.name
            this.image = image ?: user.image
            this.password = password?.let(passwordManager::encode) ?: user.password
            this.bio = bio ?: user.bio
        })
    }
}
