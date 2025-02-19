package io.github.gunkim.realworld.domain

import java.time.Instant
import java.util.UUID

interface Editable<T> {
    fun edit(): T
}

interface DateAuditable {
    val createdAt: Instant
    val updatedAt: Instant
}

interface EntityId {
    val value: UUID
}