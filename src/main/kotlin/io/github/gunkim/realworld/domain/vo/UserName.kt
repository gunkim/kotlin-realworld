package io.github.gunkim.realworld.domain.vo

import jakarta.persistence.Embeddable

@Embeddable
data class UserName(
    val value: String,
)
