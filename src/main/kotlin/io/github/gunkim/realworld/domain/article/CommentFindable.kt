package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.article.model.Comment
import io.github.gunkim.realworld.domain.article.model.CommentId
import io.github.gunkim.realworld.domain.article.repository.CommentRepository

interface CommentFindable {
    val commentRepository: CommentRepository

    fun findById(id: CommentId): Comment = commentRepository.findById(id)
        ?: throw IllegalArgumentException()
}