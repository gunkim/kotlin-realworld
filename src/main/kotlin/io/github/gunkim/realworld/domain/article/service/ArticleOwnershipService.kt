package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.exception.NotArticleAuthorException
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.user.model.UserId
import org.springframework.stereotype.Service

@Service
class ArticleOwnershipService {
    fun validateOwnership(article: Article, authUuid: UserId) {
        require(article.author.id == authUuid) {
            throw NotArticleAuthorException.of(article.id, authUuid)
        }
    }
}