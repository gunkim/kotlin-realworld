package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.DateAuditable
import io.github.gunkim.realworld.domain.Editable
import io.github.gunkim.realworld.domain.user.model.User
import java.time.Instant
import java.util.UUID

interface Article : Editable<Article>, DateAuditable {
    val uuid: UUID
    val slug: Slug
    val title: String
    val description: String
    val body: String
    val author: User
    val comments: List<Comment>
    val tags: List<Tag>

    override fun edit(): Editor

    interface Editor : Article {
        override var slug: Slug
        override var title: String
        override var description: String
        override var body: String

        override fun edit(): Editor = this
    }

    companion object {
        class Model(
            override val uuid: UUID,
            override var slug: Slug,
            override var title: String,
            override var description: String,
            override var body: String,
            override val author: User,
            override val comments: List<Comment>,
            override val tags: List<Tag>,
            override val createdAt: Instant,
            override val updatedAt: Instant,
        ) : Editor

        fun create(
            uuid: UUID = UUID.randomUUID(),
            slug: Slug,
            title: String,
            description: String,
            body: String,
            author: User,
            comments: List<Comment> = listOf(),
            tags: List<Tag> = listOf(),
            createdAt: Instant? = null,
        ): Article {
            val now = Instant.now()

            return Model(
                uuid = uuid,
                slug = slug,
                title = title,
                description = description,
                body = body,
                author = author,
                comments = comments,
                tags = tags,
                createdAt = createdAt ?: now,
                updatedAt = now
            )
        }
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

interface Tag {
    val name: String

    companion object {
        class Model(
            override val name: String,
        ) : Tag

        fun create(name: String): Tag = Model(
            name = name,
        )
    }
}
