package io.github.gunkim.realworld.domain.comment.service

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.comment.model.Comment
import io.github.gunkim.realworld.domain.user.model.User
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec

@DisplayName("Comment Validation Service Test")
class CommentValidationServiceTest : StringSpec({
    fun createTestUser(): User = User.create(
        email = "gunkim.dev@gmail.com",
        password = "<PASSWORD>!",
        name = "Gunkim"
    )

    fun createTestArticle(author: User): Article = Article.create(
        slug = Slug.fromTitle("Hello, World!"),
        title = "Hello, World!",
        description = "Hello, World!",
        body = "Hello, World!",
        author = author
    )

    fun createTestComment(article: Article, author: User): Comment = Comment.create(
        body = "Hello, World!",
        article = article,
        author = author
    )

    "valid comment belongs to article" {
        val sut = CommentValidationService()
        val user = createTestUser()
        val article = createTestArticle(user)
        val comment = createTestComment(article, user)

        shouldNotThrowAny {
            sut.validateCommentBelongsToArticle(comment, article)
        }
    }

    "invalid comment from different article throws exception" {
        val sut = CommentValidationService()
        val user = createTestUser()
        val originalArticle = createTestArticle(user)
        val differentArticle = createTestArticle(user)
        val commentFromDifferentArticle = createTestComment(differentArticle, user)

        shouldThrow<IllegalArgumentException> {
            sut.validateCommentBelongsToArticle(commentFromDifferentArticle, originalArticle)
        }
    }
})