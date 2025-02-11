package io.github.gunkim.realworld.share

open class DomainException(
    override val message: String,
) : RuntimeException(message)