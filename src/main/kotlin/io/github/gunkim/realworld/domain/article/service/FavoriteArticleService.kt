package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.ArticleReadRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FavoriteArticleService(
    private val articleReadRepository: ArticleReadRepository,
) {
    fun getFavoritesCount(articleUuids: List<UUID>): List<ArticleCountProjection> =
        articleReadRepository.getCountAllByArticleUuids(articleUuids)

    fun getFavoritesArticles(userUuid: UUID): List<UUID> = articleReadRepository.getFavoritesArticles(userUuid)
}