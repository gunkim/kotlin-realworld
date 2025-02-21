package io.github.gunkim.realworld.domain.article.repository

import io.github.gunkim.realworld.domain.article.model.Comment
import io.github.gunkim.realworld.domain.article.model.CommentId

interface CommentReadRepository {
    fun findById(commentId: CommentId): Comment?
}