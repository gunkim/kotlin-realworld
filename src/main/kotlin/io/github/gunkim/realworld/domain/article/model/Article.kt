package io.github.gunkim.realworld.domain.article.model

import io.github.gunkim.realworld.domain.DateAuditable
import io.github.gunkim.realworld.domain.Editable
import io.github.gunkim.realworld.domain.EntityId
import io.github.gunkim.realworld.domain.user.model.User
import java.time.Instant
import java.util.UUID

data class ArticleId(
    override val value: UUID,
) : EntityId<UUID> {
    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun create(): ArticleId = ArticleId(UUID.randomUUID())
    }
}

interface Article : Editable<Article>, DateAuditable {
    val id: ArticleId
    val slug: Slug
    val title: String
    val description: String
    val body: String
    val author: User
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
            override val id: ArticleId,
            override var slug: Slug,
            override var title: String,
            override var description: String,
            override var body: String,
            override val author: User,
            override val tags: List<Tag>,
            override val createdAt: Instant,
            override val updatedAt: Instant,
        ) : Editor

        fun create(
            id: ArticleId = ArticleId.create(),
            slug: Slug,
            title: String,
            description: String,
            body: String,
            author: User,
            tags: List<Tag> = listOf(),
            createdAt: Instant? = null,
        ): Article {
            val now = Instant.now()

            return Model(
                id = id,
                slug = slug,
                title = title,
                description = description,
                body = body,
                author = author,
                tags = tags,
                createdAt = createdAt ?: now,
                updatedAt = now
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
