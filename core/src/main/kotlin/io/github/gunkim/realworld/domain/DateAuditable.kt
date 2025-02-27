package io.github.gunkim.realworld.domain

import java.time.Instant

interface DateAuditable {
    val createdAt: Instant
    val updatedAt: Instant
}