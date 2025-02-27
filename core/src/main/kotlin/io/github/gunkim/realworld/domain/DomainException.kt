package io.github.gunkim.realworld.domain

open class DomainException(
    override val message: String,
) : RuntimeException(message)