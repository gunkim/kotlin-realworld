package io.github.gunkim.realworld.domain.article.repository

import io.github.gunkim.realworld.domain.article.model.Comment


interface CommentRepository : CommentReadRepository {
    fun save(comment: Comment): Comment
    fun delete(comment: Comment)
}