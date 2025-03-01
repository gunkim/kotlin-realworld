package io.github.gunkim.realworld.infrastructure.jpa.article.dao

import io.github.gunkim.realworld.infrastructure.jpa.article.model.FavoriteEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleFavoriteDao : JpaRepository<FavoriteEntity, Int> {
    fun deleteByArticleEntityDatabaseIdAndUserEntityDatabaseId(articleDatabaseId: Int, userDatabaseId: Int)
}