package io.github.gunkim.realworld.domain.article.model

import io.github.gunkim.realworld.domain.DateAuditable
import io.github.gunkim.realworld.domain.EntityId
import io.github.gunkim.realworld.domain.user.model.User
import java.time.Instant

data class CommentId internal constructor(
    override val value: Int,
) : EntityId<Int> {
    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun dummy(): CommentId = CommentId(-1)
    }
}

interface Comment : DateAuditable {
    val id: CommentId
    val body: String
    val author: User
    val article: Article

    interface Init : Comment {
        override val id: CommentId
            get() = throw UnsupportedOperationException("The initial Comment does not have an ID.")
    }

    companion object {
        class Model(
            override val id: CommentId,
            override var body: String,
            override val article: Article,
            override val author: User,
            override val createdAt: Instant,
            override val updatedAt: Instant,
        ) : Init

        fun create(
            body: String,
            author: User,
            article: Article,
            createdAt: Instant? = null,
            updatedAt: Instant? = null,
        ): Comment {
            val now = Instant.now()

            return Model(
                id = CommentId.dummy(),
                body = body,
                author = author,
                article = article,
                createdAt = createdAt ?: now,
                updatedAt = updatedAt ?: now
            )
        }
    }
}
