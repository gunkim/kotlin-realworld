package io.github.gunkim.realworld.domain.comment.repository

import io.github.gunkim.realworld.domain.comment.model.Comment


interface CommentRepository : CommentReadRepository {
    fun save(comment: Comment): Comment
    fun delete(comment: Comment)
}