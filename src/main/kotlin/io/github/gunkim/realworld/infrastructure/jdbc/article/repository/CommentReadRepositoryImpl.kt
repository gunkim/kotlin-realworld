package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.model.Comment
import io.github.gunkim.realworld.domain.article.model.CommentId
import io.github.gunkim.realworld.domain.article.repository.CommentReadRepository
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.CommentDao
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class CommentReadRepositoryImpl(
    private val commentDao: CommentDao,
) : CommentReadRepository {
    override fun findById(commentId: CommentId): Comment? {
        return commentDao.findById(commentId.value).getOrNull()
    }
}