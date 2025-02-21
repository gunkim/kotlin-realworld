package io.github.gunkim.realworld.domain.comment.repository

import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.comment.model.CommentId

interface CommentReadRepository {
    fun findById(commentId: CommentId): Comment?
}