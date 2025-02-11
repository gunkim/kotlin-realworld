package io.github.gunkim.realworld.domain.user.model

import io.github.gunkim.realworld.domain.DateAuditable
import io.github.gunkim.realworld.domain.Editable
import java.net.URL
import java.time.Instant
import java.util.UUID

interface User : Editable<User>, DateAuditable {
    val uuid: UUID
    val email: String
    val password: String
    val name: String
    val bio: String?
    val image: URL?

    override fun edit(): Editor

    interface Editor : User {
        override var email: String
        override var name: String
        override var bio: String?
        override var image: URL?
        override var password: String

        override var updatedAt: Instant

        override fun edit(): Editor = this
    }

    companion object {
        internal data class Model(
            override val uuid: UUID,
            override var email: String,
            override var password: String,
            override var name: String,
            override var bio: String?,
            override var image: URL?,
            override val createdAt: Instant,
            override var updatedAt: Instant,
        ) : Editor

        fun create(
            uuid: UUID = UUID.randomUUID(),
            email: String,
            password: String,
            name: String,
            bio: String? = null,
            image: URL? = null,
            createdAt: Instant? = null,
            updatedAt: Instant? = null,
        ): User {
            val now = Instant.now()

            return Model(
                uuid = uuid,
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