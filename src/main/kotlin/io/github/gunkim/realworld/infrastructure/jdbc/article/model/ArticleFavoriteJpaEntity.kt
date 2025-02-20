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