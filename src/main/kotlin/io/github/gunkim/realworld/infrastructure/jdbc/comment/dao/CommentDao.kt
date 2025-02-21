package io.github.gunkim.realworld.infrastructure.jdbc.comment.dao

import io.github.gunkim.realworld.infrastructure.jdbc.comment.model.CommentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentDao : JpaRepository<CommentJpaEntity, Int> {
}