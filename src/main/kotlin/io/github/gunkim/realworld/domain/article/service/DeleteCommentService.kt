package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.CommentFindable
import io.github.gunkim.realworld.domain.article.model.CommentId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.article.repository.CommentRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import org.springframework.stereotype.Service

@Service
class DeleteCommentService(
    override val articleRepository: ArticleRepository,
    override val commentRepository: CommentRepository,
    private val commentOwnershipService: CommentOwnershipService,
    private val commentValidationService: CommentValidationService,
) : ArticleFindable, CommentFindable {

    fun deleteComment(
        slug: Slug,
        commentId: CommentId,
        deleterId: UserId,
    ) {
        val article = findBySlug(slug)
        val comment = findById(commentId)

        commentValidationService.validateCommentBelongsToArticle(comment, article)
        commentOwnershipService.validateOwnership(comment, deleterId)

        commentRepository.delete(comment)
    }
}
