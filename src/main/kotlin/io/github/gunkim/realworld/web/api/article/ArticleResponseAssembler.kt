package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.user.service.FollowPredicate
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.article.model.response.ArticleResponse
import io.github.gunkim.realworld.web.api.article.model.response.ArticlesResponse
import org.springframework.stereotype.Component


@Component
class ArticleResponseAssembler(
    private val favoriteArticleService: FavoriteArticleService,
    private val followUserService: FollowUserService,
) {
    fun assembleArticleResponse(article: Article, authenticatedUser: AuthenticatedUser?): ArticleResponse {
        val favoritesCount = favoriteArticleService.getFavoritesCount(listOf(article.id))
            .firstOrNull { it.articleId == article.id }?.count ?: 0

        return if (authenticatedUser == null) {
            ArticleResponse.noAuthenticated(article, favoritesCount)
        } else {
            val (favoritedArticleUuids, followingPredicate) = getUserContext(authenticatedUser)
            ArticleResponse.from(
                article,
                favoritesCount,
                favoritedArticleUuids.contains(article.id),
                followingPredicate(article.author.id)
            )
        }
    }

    fun assembleArticlesResponse(articles: List<Article>, authenticatedUser: AuthenticatedUser?): ArticlesResponse {
        val articleUuids = articles.map { it.id }
        val favoritesCountMap = if (articleUuids.isNotEmpty()) {
            favoriteArticleService.getFavoritesCount(articleUuids)
        } else {
            emptyList()
        }
        val (favoritedArticleUuids, followingPredicate) = getUserContext(authenticatedUser)
        val responses = articles.map { article ->
            val favoritesCount = favoritesCountMap.firstOrNull { it.articleId == article.id }?.count ?: 0
            ArticleResponse.from(
                article,
                favoritesCount,
                favoritedArticleUuids.contains(article.id),
                followingPredicate(article.author.id)
            )
        }
        return ArticlesResponse.create(responses)
    }

    private fun getUserContext(authenticatedUser: AuthenticatedUser?): Pair<List<ArticleId>, FollowPredicate> {
        return if (authenticatedUser != null) {
            val userId = authenticatedUser.userId
            val favoritedArticleUuids = favoriteArticleService.getFavoritesArticles(userId)
            val followingPredicate = followUserService.getFollowingPredicate(userId)
            favoritedArticleUuids to followingPredicate
        } else {
            emptyList<ArticleId>() to { false }
        }
    }
}
