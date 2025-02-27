package io.github.gunkim.realworld.domain.article.exception

import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.DomainException

class NotArticleAuthorException internal constructor(
    message: String,
) : DomainException(message) {
    companion object {
        fun of(
            articleId: ArticleId,
            authUuid: UserId,
        ) = NotArticleAuthorException(
            "User with ID: $authUuid is not the author of the article with Id: $articleId"
        )
    }
}