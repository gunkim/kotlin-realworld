package io.github.gunkim.realworld.domain.user.exception

import io.github.gunkim.realworld.core.DomainException

class InvalidUserPasswordException(
    override val message: String,
) : DomainException(message)