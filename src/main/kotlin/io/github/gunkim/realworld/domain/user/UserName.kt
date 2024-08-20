package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserName(
    @Column(name = "name")
    val value: String,
) : ValueObject<UserName>()
