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
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
) {
    companion object {
        fun of(articleId: Int, userId: Int) =
            ArticleFavoriteJpaEntity(
                articleId = articleId,
                userId = userId,
                createdAt = Instant.now()
            )
    }
}