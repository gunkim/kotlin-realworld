package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.exception.NotArticleAuthorException
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec

private fun createUser(email: String, password: String, name: String): User {
    return User.create(
        email = email,
        password = password,
        name = name
    )
}

private fun createArticle(title: String, author: User): Article {
    return Article.create(
        title = title,
        slug = Slug.fromTitle(title),
        description = title,
        body = title,
        author = author
    )
}

@DisplayName("ArticleOwnershipService Test")
class ArticleOwnershipServiceTest : StringSpec({
    val articleOwnershipService = ArticleOwnershipService()
    val email = "gunkim.dev@gmail.com"
    val title = "Hello, World!"

    "should succeed when the user is the author of the article" {
        val userAuthor = createUser(email, "<PASSWORD>!", "Gunkim")
        val authoredArticle = createArticle(title, userAuthor)
        articleOwnershipService.validateOwnership(authoredArticle, userAuthor.id)
    }

    "should throw NotArticleAuthorException when the user is not the author of the article" {
        val userAuthor = createUser(email, "<PASSWORD>!", "Gunkim")
        val otherUserId = UserId.create()
        val authoredArticle = createArticle(title, userAuthor)

        shouldThrow<NotArticleAuthorException> {
            articleOwnershipService.validateOwnership(authoredArticle, otherUserId)
        }
    }
})