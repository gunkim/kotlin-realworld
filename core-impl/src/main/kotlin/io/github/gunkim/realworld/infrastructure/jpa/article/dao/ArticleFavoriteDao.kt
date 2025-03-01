package io.github.gunkim.realworld.infrastructure.jpa.article.dao

import io.github.gunkim.realworld.infrastructure.jpa.article.model.ArticleFavoriteJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleFavoriteDao : JpaRepository<ArticleFavoriteJpaEntity, Int> {
    fun deleteByArticleJpaEntityDatabaseIdAndUserJpaEntityDatabaseId(articleDatabaseId: Int, userDatabaseId: Int)
}