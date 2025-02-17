package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.Slug
import io.github.gunkim.realworld.domain.article.service.CreateArticleService
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.article.service.GetArticleService
import io.github.gunkim.realworld.domain.article.service.UpdateArticleService
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.article.model.request.CreateArticleRequest
import io.github.gunkim.realworld.web.api.article.model.request.GetArticlesRequest
import io.github.gunkim.realworld.web.api.article.model.request.UpdateArticleRequest
import io.github.gunkim.realworld.web.api.article.model.response.ArticleResponse
import io.github.gunkim.realworld.web.api.article.model.response.ArticleResponseWrapper
import io.github.gunkim.realworld.web.api.article.model.response.ArticlesResponse
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/articles")
interface ArticleResource {
    @GetMapping
    fun getArticles(
        request: GetArticlesRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser?,
    ): ArticlesResponse

    @GetMapping("/{slug}")
    fun getArticle(@PathVariable slug: String): ArticleResponseWrapper

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createArticle(
        @JsonRequest("article") request: CreateArticleRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper

    @PutMapping("/{slug}")
    fun updateArticles(
        @PathVariable slug: String,
        @JsonRequest("article") request: UpdateArticleRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper
}

@RestController
class ArticlesController(
    private val getArticleService: GetArticleService,
    private val createArticleService: CreateArticleService,
    private val favoriteArticleService: FavoriteArticleService,
    private val followUserService: FollowUserService,
    private val updateArticleService: UpdateArticleService,
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

    override fun getArticle(slug: String): ArticleResponseWrapper {
        val article = getArticleService.getArticle(Slug(slug))
        val articleUuids = listOf(article.uuid)
        val favoritesCountMap = if (articleUuids.isEmpty()) {
            emptyList()
        } else {
            favoriteArticleService.getFavoritesCount(articleUuids)
        }

        return ArticleResponse.noAuthenticated(
            article,
            favoritesCountMap.firstOrNull { it.getUuid() == article.uuid }?.getCount() ?: 0
        ).let(::ArticleResponseWrapper)
    }

    override fun createArticle(
        request: CreateArticleRequest,
        authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper {
        val article = createArticleService.createArticle(
            request.title,
            request.description,
            request.body,
            request.tagList,
            authenticatedUser.uuid
        )

        return ArticleResponse.create(article)
            .let(::ArticleResponseWrapper)
    }

    override fun updateArticles(
        slug: String,
        request: UpdateArticleRequest,
        authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper {
        val article = updateArticleService.updateArticle(
            slug = Slug(slug),
            title = request.title,
            description = request.description,
            body = request.body,
        )
        val articleUuids = listOf(article.uuid)
        val favoritesCountMap = if (articleUuids.isEmpty()) {
            emptyList()
        } else {
            favoriteArticleService.getFavoritesCount(articleUuids)
        }
        val (favoritedArticleUuids, followingUserUuids) = getUserContext(authenticatedUser)

        return ArticleResponse.from(
            article,
            favoritesCountMap.firstOrNull { it.getUuid() == article.uuid }?.getCount() ?: 0,
            favoritedArticleUuids.contains(article.uuid),
            followingUserUuids.contains(article.author.uuid)
        ).let(::ArticleResponseWrapper)
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

        return ArticlesResponse.create(articles.map { article ->
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

class SlugGenerator {
    fun generateSlug(title: String): String {
        return "%s-%s".format(
            title
                .lowercase() // 1. 소문자로 변환
                .replace(Regex("[^a-z0-9\\s-]"), "") // 2. 특수문자 제거 (하이픈과 공백 제외)
                .replace(Regex("\\s+"), "-") // 3. 공백을 하이픈으로 변환
                .replace(Regex("-+"), "-") // 4. 연속된 하이픈을 하나로
                .trim('-'), generateRandomString()
        )
    }

    private fun generateRandomString(): String {
        return UUID.randomUUID().toString().take(8)
    }
}