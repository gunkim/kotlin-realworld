package io.github.gunkim.realworld.infrastructure.jdbc.comment.model

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.comment.model.CommentId
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "comment")
class CommentJpaEntity(
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    body: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId", nullable = false)
    override val author: UserJpaEntity,
    article: ArticleJpaEntity? = null,
    override val createdAt: Instant,
    override var updatedAt: Instant = Instant.now(),
) : Comment {
    override val id: CommentId
        get() = CommentId.from(databaseId!!)

    override val article: Article
        get() = articleJpaEntity!!

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articleId", nullable = false)
    val articleJpaEntity: ArticleJpaEntity? = article

    override var body = body
        set(value) {
            field = value
            updatedAt = Instant.now()
        }

    companion object {
        fun from(
            comment: Comment,
        ): CommentJpaEntity {
            val now = Instant.now()
            return CommentJpaEntity(
                body = comment.body,
                author = comment.author.let(UserJpaEntity.Companion::from),
                article = comment.article.let(ArticleJpaEntity.Companion::from),
                createdAt = now,
                updatedAt = now,
            )
        }
    }
}