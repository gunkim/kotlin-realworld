package io.github.gunkim.realworld.infrastructure.jpa.article.model

import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "article_favorite")
class ArticleFavoriteJpaEntity(
    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    val articleJpaEntity: ArticleJpaEntity? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val userJpaEntity: UserJpaEntity? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleFavoriteJpaEntity

        if (databaseId != other.databaseId) return false
        if (articleJpaEntity != other.articleJpaEntity) return false
        if (userJpaEntity != other.userJpaEntity) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + (articleJpaEntity?.hashCode() ?: 0)
        result = 31 * result + (userJpaEntity?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

    companion object {
        fun of(articleJpaEntity: ArticleJpaEntity, userJpaEntity: UserJpaEntity): ArticleFavoriteJpaEntity {
            val now = Instant.now()

            return ArticleFavoriteJpaEntity(
                articleJpaEntity = articleJpaEntity,
                userJpaEntity = userJpaEntity,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}