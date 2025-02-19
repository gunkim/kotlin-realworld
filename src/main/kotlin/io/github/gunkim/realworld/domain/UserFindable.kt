package io.github.gunkim.realworld.domain

import io.github.gunkim.realworld.domain.user.exception.UserNotFoundException
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserReadRepository

internal interface UserFindable {
    val userRepository: UserReadRepository

    fun getUserById(userId: UserId): User =
        userRepository.findById(userId) ?: throw UserNotFoundException.fromId(userId)

    fun getUserByName(userName: String): User =
        userRepository.findByUserName(userName) ?: throw UserNotFoundException.fromUserName(userName)
}