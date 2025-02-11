package io.github.gunkim.realworld.core

open class DomainException(
    override val message: String,
) : RuntimeException(message)