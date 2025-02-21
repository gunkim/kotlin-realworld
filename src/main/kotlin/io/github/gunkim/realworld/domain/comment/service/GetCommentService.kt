package io.github.gunkim.realworld.domain.comment.service

import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.comment.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class GetCommentService(
    private val commentRepository: CommentRepository,
) {
    fun getComments(slug: Slug): List<Comment> =
        commentRepository.findByArticleSlug(slug)
}