package io.github.gunkim.realworld.domain.user.exception

import io.github.gunkim.realworld.core.DomainException
import java.util.UUID

class UserNotFoundException(message: String) : DomainException(message) {
    companion object {
        fun fromEmail(email: String) = UserNotFoundException("User(email: $email) not found")
        fun fromUUID(uuid: UUID): UserNotFoundException = UserNotFoundException("User(uuid: $uuid) not found")
        fun fromUserName(userName: String) = UserNotFoundException("User(name: $userName) not found")
    }
}