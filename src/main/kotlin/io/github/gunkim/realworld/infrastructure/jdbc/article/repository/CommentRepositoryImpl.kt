package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.model.Comment
import io.github.gunkim.realworld.domain.article.repository.CommentReadRepository
import io.github.gunkim.realworld.domain.article.repository.CommentRepository
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.CommentDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.CommentJpaEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CommentRepositoryImpl(
    private val commentDao: CommentDao,
    @Qualifier("commentReadRepositoryImpl")
    private val commentReadRepository: CommentReadRepository,
) : CommentRepository, CommentReadRepository by commentReadRepository {
    override fun save(comment: Comment): Comment {
        return commentDao.save(CommentJpaEntity.from(comment))
    }

    override fun delete(comment: Comment) {
        commentDao.deleteById(comment.id.value)
    }
}