package io.github.gunkim.realworld.domain.vo

import jakarta.persistence.Embeddable

@Embeddable
data class Email(
    val value: String,
)
