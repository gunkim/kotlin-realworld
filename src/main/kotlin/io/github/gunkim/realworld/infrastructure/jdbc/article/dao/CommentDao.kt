package io.github.gunkim.realworld.infrastructure.jdbc.article.dao

import io.github.gunkim.realworld.infrastructure.jdbc.article.model.CommentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentDao : JpaRepository<CommentJpaEntity, Int> {
}