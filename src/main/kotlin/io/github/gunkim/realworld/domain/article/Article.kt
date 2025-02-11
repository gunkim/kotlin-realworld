package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.DateAuditable
import io.github.gunkim.realworld.domain.Editable
import io.github.gunkim.realworld.domain.user.model.User
import java.time.Instant
import java.util.UUID

interface Article : Editable<Article>, DateAuditable {
    val uuid: UUID
    val title: String
    val description: String
    val body: String
    val author: User
    val comments: List<Comment>
    val tags: List<Tag>

    override fun edit(): Article = this

    interface Editor : Article {
        override var title: String
        override var description: String
        override var body: String

        override fun edit(): Article = this
    }
}

interface Comment : Editable<Comment>, DateAuditable {
    val uuid: UUID
    val body: String
    val author: User

    override fun edit(): Comment = this

    interface Editor : Comment {
        override var body: String

        override fun edit(): Comment = this
    }

    companion object {
        class Model(
            override val uuid: UUID,
            override var body: String,
            override val author: User,
            override val createdAt: Instant,
            override val updatedAt: Instant,
        ) : Editor

        fun create(
            uuid: UUID = UUID.randomUUID(),
            body: String,
            author: User,
            createdAt: Instant? = null,
            updatedAt: Instant? = null,
        ): Comment {
            val now = Instant.now()

            return Model(
                uuid = uuid,
                body = body,
                author = author,
                createdAt = createdAt ?: now,
                updatedAt = updatedAt ?: now
            )
        }
    }
}

interface Tag : DateAuditable {
    val name: String

    companion object {
        class Model(
            override val name: String,
            override val createdAt: Instant,
            override val updatedAt: Instant,
        ) : Tag

        fun create(
            name: String,
            createdAt: Instant? = null,
            updatedAt: Instant? = null,
        ): Tag {
            val now = Instant.now()
            return Model(
                name = name,
                createdAt = createdAt ?: now,
                updatedAt = updatedAt ?: now
            )
        }
    }
}
