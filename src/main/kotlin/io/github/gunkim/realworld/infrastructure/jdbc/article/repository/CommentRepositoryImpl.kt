package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.model.Comment
import io.github.gunkim.realworld.domain.article.repository.CommentRepository
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.CommentDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.CommentJpaEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CommentRepositoryImpl(
    private val commentDao: CommentDao,
) : CommentRepository {
    override fun save(comment: Comment): Comment {
        return commentDao.save(CommentJpaEntity.from(comment))
    }
}