package io.github.gunkim.realworld.domain

import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository
import java.util.UUID

internal interface UserFindable {
    val userRepository: UserReadRepository

    fun getUserByUUID(uuid: UUID): User =
        userRepository.findByUuid(uuid) ?: throw UserNotFoundException.fromUUID(uuid)
}