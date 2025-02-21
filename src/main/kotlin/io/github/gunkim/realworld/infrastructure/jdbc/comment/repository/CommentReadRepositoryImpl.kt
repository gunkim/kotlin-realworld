package io.github.gunkim.realworld.infrastructure.jdbc.comment.repository

import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.comment.model.CommentId
import io.github.gunkim.realworld.domain.comment.repository.CommentReadRepository
import io.github.gunkim.realworld.infrastructure.jdbc.comment.dao.CommentDao
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class CommentReadRepositoryImpl(
    private val commentDao: CommentDao,
) : CommentReadRepository {
    override fun findById(commentId: CommentId): Comment? =
        commentDao.findById(commentId.value).getOrNull()

    override fun findByArticleSlug(slug: Slug): List<Comment> =
        commentDao.findByArticleJpaEntitySlug(slug)
}