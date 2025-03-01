package io.github.gunkim.realworld.infrastructure.jpa.comment.model

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.comment.model.CommentId
import io.github.gunkim.realworld.infrastructure.jpa.article.model.ArticleEntity
import io.github.gunkim.realworld.infrastructure.jpa.share.Updatable
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserEntity
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
class CommentEntity(
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    body: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    override val author: UserEntity,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articleId")
    val articleEntity: ArticleEntity? = null,
    override val createdAt: Instant,
    override var updatedAt: Instant = Instant.now(),
) : Comment, Updatable {
    override val id: CommentId
        get() = CommentId.from(databaseId!!)

    override val article: Article
        get() = articleEntity!!

    override var body = body
        set(value) {
            field = updateField(field, value)
        }

    companion object {
        fun from(
            comment: Comment,
        ): CommentEntity {
            val now = Instant.now()
            return CommentEntity(
                body = comment.body,
                author = comment.author.let(UserEntity.Companion::from),
                articleEntity = comment.article.let(ArticleEntity.Companion::from),
                createdAt = now,
                updatedAt = now,
            )
        }
    }
}