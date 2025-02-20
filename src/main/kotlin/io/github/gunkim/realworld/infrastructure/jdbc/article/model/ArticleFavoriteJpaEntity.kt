package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "article_favorite")
class ArticleFavoriteJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val favoriteId: Int? = null,
    val articleId: Int,
    val userId: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun of(articleId: Int, userId: Int): ArticleFavoriteJpaEntity {
            val now = Instant.now()

            return ArticleFavoriteJpaEntity(
                articleId = articleId,
                userId = userId,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}