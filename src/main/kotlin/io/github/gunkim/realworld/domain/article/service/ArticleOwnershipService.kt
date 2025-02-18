package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.exception.NotArticleAuthorException
import io.github.gunkim.realworld.domain.article.model.Article
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class ArticleOwnershipService {
    fun validateOwnership(article: Article, authUuid: UUID) {
        require(article.author.uuid == authUuid) {
            throw NotArticleAuthorException.fromArticleUuidAndAuthUuid(article.uuid, authUuid)
        }
    }
}