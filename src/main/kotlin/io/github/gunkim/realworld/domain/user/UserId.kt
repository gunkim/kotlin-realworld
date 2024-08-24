package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.ValueObject
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
data class UserId(
    @Column(name = "id")
    val value: UUID = UUID.randomUUID(),
) : ValueObject<UserId>()