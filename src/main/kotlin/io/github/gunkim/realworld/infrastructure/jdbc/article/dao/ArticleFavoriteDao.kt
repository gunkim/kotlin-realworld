package io.github.gunkim.realworld.infrastructure.jdbc.article.dao

import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleFavoriteJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleFavoriteDao : JpaRepository<ArticleFavoriteJpaEntity, Int> {
}