package io.github.gunkim.realworld.infrastructure.jpa.share

import java.time.Instant

interface Updatable {
    var updatedAt: Instant

    fun <T> updateField(oldValue: T, newValue: T): T {
        if (oldValue != newValue) update()
        return newValue
    }

    private fun update() {
        updatedAt = Instant.now()
    }
}