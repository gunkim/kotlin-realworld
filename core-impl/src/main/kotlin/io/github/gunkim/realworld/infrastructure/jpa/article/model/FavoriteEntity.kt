package io.github.gunkim.realworld.infrastructure.jpa.article.model

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

@Entity(name = "article_favorite")
class FavoriteEntity(
    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    val articleEntity: ArticleEntity? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val userEntity: UserEntity? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FavoriteEntity

        if (databaseId != other.databaseId) return false
        if (articleEntity != other.articleEntity) return false
        if (userEntity != other.userEntity) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = databaseId ?: 0
        result = 31 * result + (articleEntity?.hashCode() ?: 0)
        result = 31 * result + (userEntity?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

    companion object {
        fun of(articleEntity: ArticleEntity, userEntity: UserEntity): FavoriteEntity {
            val now = Instant.now()

            return FavoriteEntity(
                articleEntity = articleEntity,
                userEntity = userEntity,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}