package io.github.gunkim.realworld.domain.comment

import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.comment.model.CommentId
import io.github.gunkim.realworld.domain.comment.repository.CommentRepository

interface CommentFindable {
    val commentRepository: CommentRepository

    fun findById(id: CommentId): Comment = commentRepository.findById(id)
        ?: throw IllegalArgumentException()
}