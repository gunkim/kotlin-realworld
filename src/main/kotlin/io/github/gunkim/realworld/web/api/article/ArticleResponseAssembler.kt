package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.article.model.response.ArticleResponse
import io.github.gunkim.realworld.web.api.article.model.response.ArticlesResponse
import java.util.UUID
import org.springframework.stereotype.Component


@Component
class ArticleResponseAssembler(
    private val favoriteArticleService: FavoriteArticleService,
    private val followUserService: FollowUserService,
) {
    fun assembleArticleResponse(article: Article, authenticatedUser: AuthenticatedUser?): ArticleResponse {
        val favoritesCount = favoriteArticleService.getFavoritesCount(listOf(article.uuid))
            .firstOrNull { it.getUuid() == article.uuid }?.getCount() ?: 0

        return if (authenticatedUser == null) {
            ArticleResponse.noAuthenticated(article, favoritesCount)
        } else {
            val (favoritedArticleUuids, followingUserUuids) = getUserContext(authenticatedUser)
            ArticleResponse.from(
                article,
                favoritesCount,
                favoritedArticleUuids.contains(article.uuid),
                followingUserUuids.contains(article.author.uuid)
            )
        }
    }

    fun assembleArticlesResponse(articles: List<Article>, authenticatedUser: AuthenticatedUser?): ArticlesResponse {
        val articleUuids = articles.map { it.uuid }
        val favoritesCountMap = if (articleUuids.isNotEmpty()) {
            favoriteArticleService.getFavoritesCount(articleUuids)
        } else {
            emptyList()
        }
        val (favoritedArticleUuids, followingUserUuids) = getUserContext(authenticatedUser)
        val responses = articles.map { article ->
            val favoritesCount = favoritesCountMap.firstOrNull { it.getUuid() == article.uuid }?.getCount() ?: 0
            ArticleResponse.from(
                article,
                favoritesCount,
                favoritedArticleUuids.contains(article.uuid),
                followingUserUuids.contains(article.author.uuid)
            )
        }
        return ArticlesResponse.create(responses)
    }

    private fun getUserContext(authenticatedUser: AuthenticatedUser?): Pair<List<UUID>, List<UUID>> {
        return if (authenticatedUser != null) {
            val userUuid = authenticatedUser.uuid
            val favoritedArticleUuids = favoriteArticleService.getFavoritesArticles(userUuid)
            val followingUserUuids = followUserService.getFollowingUserUuids(userUuid)
            favoritedArticleUuids to followingUserUuids
        } else {
            emptyList<UUID>() to emptyList()
        }
    }
}
