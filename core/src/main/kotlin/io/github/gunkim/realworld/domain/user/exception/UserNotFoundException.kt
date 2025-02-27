package io.github.gunkim.realworld.domain.user.exception

import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.DomainException

class UserNotFoundException(message: String) : DomainException(message) {
    companion object {
        fun fromEmail(email: String) = UserNotFoundException("User(email: $email) not found")
        fun fromId(userId: UserId): UserNotFoundException = UserNotFoundException("User(uuid: $userId) not found")
        fun fromUserName(userName: String) = UserNotFoundException("User(name: $userName) not found")
    }
}