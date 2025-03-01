package io.github.gunkim.realworld.infrastructure.jpa.comment.repository

import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.comment.repository.CommentReadRepository
import io.github.gunkim.realworld.domain.comment.repository.CommentRepository
import io.github.gunkim.realworld.infrastructure.jpa.comment.dao.CommentDao
import io.github.gunkim.realworld.infrastructure.jpa.comment.model.CommentEntity
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
        return commentDao.save(CommentEntity.from(comment))
    }

    override fun delete(comment: Comment) {
        commentDao.deleteById(comment.id.value)
    }
}