package io.github.gunkim.realworld.domain

import java.time.Instant

interface Editable<T> {
    fun edit(): T
}

interface DateAuditable {
    val createdAt: Instant
    val updatedAt: Instant
}

interface EntityId<T> {
    val value: T
}