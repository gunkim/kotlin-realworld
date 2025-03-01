package io.github.gunkim.realworld.infrastructure.jpa.comment.dao

import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.infrastructure.jpa.comment.model.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentDao : JpaRepository<CommentEntity, Int> {
    fun findByArticleEntitySlug(slug: Slug): List<CommentEntity>
}