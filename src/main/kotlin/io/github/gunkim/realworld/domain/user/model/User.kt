package io.github.gunkim.realworld.domain.user.model

import io.github.gunkim.realworld.domain.DateAuditable
import io.github.gunkim.realworld.domain.Editable
import io.github.gunkim.realworld.domain.EntityId
import java.time.Instant
import java.util.UUID

data class UserId internal constructor(
    override val value: UUID,
) : EntityId<UUID> {
    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun create(): UserId = UserId(UUID.randomUUID())
    }
}

interface User : Editable<User>, DateAuditable {
    val id: UserId
    val email: String
    val password: String
    val name: String
    val bio: String?
    val image: String?

    override fun edit(): Editor

    interface Editor : User {
        override var email: String
        override var name: String
        override var bio: String?
        override var image: String?
        override var password: String

        override var updatedAt: Instant

        override fun edit(): Editor = this
    }

    companion object {
        internal data class Model(
            override val id: UserId,
            override var email: String,
            override var password: String,
            override var name: String,
            override var bio: String?,
            override var image: String?,
            override val createdAt: Instant,
            override var updatedAt: Instant,
        ) : Editor

        fun create(
            id: UserId = UserId.create(),
            email: String,
            password: String,
            name: String,
            bio: String? = null,
            image: String? = null,
            createdAt: Instant? = null,
            updatedAt: Instant? = null,
        ): User {
            val now = Instant.now()

            return Model(
                id = id,
                email = email,
                password = password,
                name = name,
                bio = bio,
                image = image,
                createdAt = createdAt ?: now,
                updatedAt = updatedAt ?: now
            )
        }
    }
}