package io.github.gunkim.realworld.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Email(
    @Column(name = "email")
    val value: String,
)
