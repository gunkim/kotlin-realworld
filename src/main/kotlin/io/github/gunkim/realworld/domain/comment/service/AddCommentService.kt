package io.github.gunkim.realworld.domain.comment.service

import io.github.gunkim.realworld.domain.UserFindable
import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.comment.repository.CommentRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AddCommentService(
    override val articleRepository: ArticleRepository,
    override val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
) : ArticleFindable, UserFindable {
    fun addComment(
        slug: Slug,
        commentBody: String,
        authorId: UserId,
    ): Comment {
        val article = findBySlug(slug)
        val author = getUserById(authorId)

        return commentRepository.save(
            Comment.create(
                body = commentBody,
                article = article,
                author = author,
            )
        )
    }
}