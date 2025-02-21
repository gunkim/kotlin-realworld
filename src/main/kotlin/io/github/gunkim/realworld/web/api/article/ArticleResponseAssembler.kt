package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.article.service.FavoritePredicate
import io.github.gunkim.realworld.domain.article.service.FavoritesCounter
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
        val favoritesCounter = favoriteArticleService.getFavoritesCounter(listOf(article.id))
        val (favoritePredicate, followingPredicate) = getUserContext(authenticatedUser)
        return createArticleResponse(article, favoritesCounter, favoritePredicate, followingPredicate)
    }

    fun assembleArticlesResponse(articles: List<Article>, authenticatedUser: AuthenticatedUser?): ArticlesResponse {
        if (articles.isEmpty()) return ArticlesResponse.create(emptyList())

        val articleIds = articles.map { it.id }
        val favoritesCounter = favoriteArticleService.getFavoritesCounter(articleIds)
        val (favoritePredicate, followingPredicate) = getUserContext(authenticatedUser)

        val responses = articles.map { article ->
            createArticleResponse(
                article,
                favoritesCounter,
                favoritePredicate,
                followingPredicate
            )
        }

        return ArticlesResponse.create(responses)
    }

    private fun createArticleResponse(
        article: Article,
        favoritesCounter: FavoritesCounter,
        favoritePredicate: FavoritePredicate,
        followingPredicate: FollowPredicate,
    ): ArticleResponse {
        return ArticleResponse.from(
            article,
            favoritesCounter(article.id),
            favoritePredicate(article.id),
            followingPredicate(article.author.id)
        )
    }

    private fun getUserContext(authenticatedUser: AuthenticatedUser?): Pair<FavoritePredicate, FollowPredicate> {
        return if (authenticatedUser != null) {
            val userId = authenticatedUser.userId
            favoriteArticleService.getFavoritePredicate(userId) to followUserService.getFollowingPredicate(userId)
        } else {
            Pair({ false }, { false })
        }
    }
}
