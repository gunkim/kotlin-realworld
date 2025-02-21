package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "article_favorite")
class ArticleFavoriteJpaEntity(
    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    @Column(name = "article_id")
    val articleDatabaseId: Int,
    @Column(name = "user_id")
    val userDatabaseId: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleFavoriteJpaEntity

        if (databaseId != other.databaseId) return false
        if (articleDatabaseId != other.articleDatabaseId) return false
        if (userDatabaseId != other.userDatabaseId) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + articleDatabaseId
        result = 31 * result + userDatabaseId
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

    companion object {
        fun of(articleId: Int, userId: Int): ArticleFavoriteJpaEntity {
            val now = Instant.now()

            return ArticleFavoriteJpaEntity(
                articleDatabaseId = articleId,
                userDatabaseId = userId,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}