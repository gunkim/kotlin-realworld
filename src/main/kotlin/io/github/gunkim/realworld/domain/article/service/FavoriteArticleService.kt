package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.ArticleFindable
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.service.GetUserService
import org.springframework.stereotype.Service

typealias FavoritePredicate = (ArticleId) -> Boolean
typealias FavoritesCounter = (ArticleId) -> Int

@Service
class FavoriteArticleService(
    override val articleRepository: ArticleRepository,
    private val getUserService: GetUserService,
) : ArticleFindable {
    fun getFavoritesCounter(articleIds: List<ArticleId>): FavoritesCounter {
        val favoritesCountMap = articleRepository.getCountAllByArticleIds(articleIds)
            .associate { it.articleId to it.count }
        return { articleId -> favoritesCountMap[articleId] ?: 0 }
    }

    fun getFavoritePredicate(userId: UserId): FavoritePredicate {
        val favoriteArticleIds = articleRepository.getFavoritesArticleIds(userId)
        return favoriteArticleIds::contains
    }

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