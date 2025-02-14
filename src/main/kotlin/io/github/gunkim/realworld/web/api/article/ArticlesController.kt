package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.article.service.GetArticleService
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.article.model.request.GetArticlesRequest
import io.github.gunkim.realworld.web.api.article.model.response.ArticleResponse
import io.github.gunkim.realworld.web.api.article.model.response.ArticlesResponse
import java.util.UUID
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/articles")
interface ArticleResource {
    @GetMapping
    fun getArticles(
        request: GetArticlesRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser?,
    ): ArticlesResponse
}

@RestController
class ArticlesController(
    private val getArticleService: GetArticleService,
    private val favoriteArticleService: FavoriteArticleService,
    private val followUserService: FollowUserService,
) : ArticleResource {
    override fun getArticles(
        request: GetArticlesRequest,
        authenticatedUser: AuthenticatedUser?,
    ): ArticlesResponse {
        val articles = getArticleService.getArticles(
            tag = request.tag,
            author = request.author,
            offset = request.offset,
            limit = request.limit,
        )
        return articlesResponse(articles, authenticatedUser)
    }

    private fun articlesResponse(
        articles: List<Article>,
        authenticatedUser: AuthenticatedUser?,
    ): ArticlesResponse {
        val articleUuids = articles.map(Article::uuid)
        val favoritesCountMap = if (articleUuids.isEmpty()) {
            emptyList()
        } else {
            favoriteArticleService.getFavoritesCount(articleUuids)
        }
        val (favoritedArticleUuids, followingUserUuids) = getUserContext(authenticatedUser)

        return ArticlesResponse(articles.map { article ->
            ArticleResponse.from(
                article,
                favoritesCountMap.firstOrNull { it.getUuid() == article.uuid }?.getCount() ?: 0,
                favoritedArticleUuids.contains(article.uuid),
                followingUserUuids.contains(article.author.uuid)
            )
        })
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