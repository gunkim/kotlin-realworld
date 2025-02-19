package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.UserId
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

    fun getFavoritesArticles(userId: UserId): List<UUID> = articleRepository.getFavoritesArticles(userId)

    fun favoriteArticle(
        slug: Slug,
        favoritingId: UserId,
    ): Article {
        val article = super.findBySlug(slug)
        val user = getUserService.getUserById(favoritingId)

        articleRepository.favorite(article, user)
        return article
    }

    fun unfavoriteArticle(
        slug: Slug,
        unavoritingId: UserId,
    ): Article {
        val article = super.findBySlug(slug)
        val user = getUserService.getUserById(unavoritingId)

        articleRepository.unFavorite(article, user)
        return article
    }
}