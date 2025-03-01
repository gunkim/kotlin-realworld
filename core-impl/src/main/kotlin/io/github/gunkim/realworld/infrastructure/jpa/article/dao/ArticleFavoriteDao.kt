package io.github.gunkim.realworld.infrastructure.jpa.article.dao

import io.github.gunkim.realworld.infrastructure.jpa.article.model.ArticleEntity
import io.github.gunkim.realworld.infrastructure.jpa.article.model.FavoriteEntity
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleFavoriteDao : JpaRepository<FavoriteEntity, Int> {
    fun deleteByArticleEntityAndUserEntity(articleEntity: ArticleEntity, userEntity: UserEntity)
}