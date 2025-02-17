package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.model.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FavoriteArticleService(
    private val articleRepository: ArticleRepository,
) {
    fun getFavoritesCount(articleUuids: List<UUID>): List<ArticleCountProjection> =
        articleRepository.getCountAllByArticleUuids(articleUuids)

    fun getFavoritesArticles(userUuid: UUID): List<UUID> = articleRepository.getFavoritesArticles(userUuid)
}