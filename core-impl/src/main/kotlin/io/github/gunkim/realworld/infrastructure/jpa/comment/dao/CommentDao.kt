package io.github.gunkim.realworld.infrastructure.jpa.comment.dao

import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.infrastructure.jpa.comment.model.CommentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentDao : JpaRepository<CommentJpaEntity, Int> {
    fun findByArticleJpaEntitySlug(slug: Slug): List<CommentJpaEntity>
}