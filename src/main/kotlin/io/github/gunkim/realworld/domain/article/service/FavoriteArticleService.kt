package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.service.GetUserService
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FavoriteArticleService(
    override val articleRepository: ArticleRepository,
    private val getUserService: GetUserService,
) : ArticleFindable {
    fun getFavoritesCount(articleUuids: List<UUID>): List<ArticleCountProjection> =
        articleRepository.getCountAllByArticleUuids(articleUuids)

    fun getFavoritesArticles(userUuid: UUID): List<UUID> = articleRepository.getFavoritesArticles(userUuid)

    fun favoriteArticle(
        slug: Slug,
        userUuid: UUID,
    ): Article {
        val article = super.findBySlug(slug)
        val user = getUserService.getUserByUUID(userUuid)

        articleRepository.favorite(article, user)
        return article
    }

    fun unfavoriteArticle(
        slug: Slug,
        userUuid: UUID,
    ): Article {
        val article = super.findBySlug(slug)
        val user = getUserService.getUserByUUID(userUuid)

        articleRepository.unFavorite(article, user)
        return article
    }
}