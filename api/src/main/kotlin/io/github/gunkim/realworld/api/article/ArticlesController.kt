package io.github.gunkim.realworld.api.article

import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.service.CreateArticleService
import io.github.gunkim.realworld.domain.article.service.DeleteArticleService
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.article.service.GetArticleService
import io.github.gunkim.realworld.domain.article.service.UpdateArticleService
import io.github.gunkim.realworld.api.AuthenticatedUser
import io.github.gunkim.realworld.api.JsonRequest
import io.github.gunkim.realworld.api.article.model.request.CreateArticleRequest
import io.github.gunkim.realworld.api.article.model.request.FeedArticleRequest
import io.github.gunkim.realworld.api.article.model.request.GetArticlesRequest
import io.github.gunkim.realworld.api.article.model.request.UpdateArticleRequest
import io.github.gunkim.realworld.api.article.model.response.wrapper.ArticleWrapper
import io.github.gunkim.realworld.api.article.model.response.wrapper.ArticlesWrapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
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
    ): ArticlesWrapper

    @GetMapping("/feed")
    fun feedArticles(
        @ModelAttribute request: FeedArticleRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticlesWrapper

    @GetMapping("/{slug}")
    fun getArticle(@PathVariable slug: String): ArticleWrapper

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createArticle(
        @JsonRequest("article") request: CreateArticleRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper

    @PutMapping("/{slug}")
    fun updateArticle(
        @PathVariable slug: String,
        @JsonRequest("article") request: UpdateArticleRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper

    @DeleteMapping("/{slug}")
    fun deleteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    )

    @PostMapping("/{slug}/favorite")
    fun favoriteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper

    @DeleteMapping("/{slug}/favorite")
    fun unFavoriteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper
}

@RestController
class ArticlesController(
    private val getArticleService: GetArticleService,
    private val createArticleService: CreateArticleService,
    private val updateArticleService: UpdateArticleService,
    private val deleteArticleService: DeleteArticleService,
    private val articleResponseAssembler: ArticleResponseAssembler,
    private val favoriteArticleService: FavoriteArticleService,
) : ArticleResource {
    override fun getArticles(
        request: GetArticlesRequest,
        authenticatedUser: AuthenticatedUser?,
    ): ArticlesWrapper {
        val articles = getArticleService.getArticles(
            tag = request.tag,
            author = request.author,
            offset = request.offset,
            limit = request.limit,
            favoritedUsername = request.favorited
        )
        return articleResponseAssembler.assembleArticlesResponse(articles, authenticatedUser)
    }

    override fun getArticle(slug: String): ArticleWrapper {
        val article = getArticleService.getArticle(Slug.from(slug))
        return articleResponseAssembler.assembleArticleResponse(article, null)
            .let(::ArticleWrapper)
    }

    override fun createArticle(
        request: CreateArticleRequest,
        authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper {
        val article = createArticleService.createArticle(
            request.title,
            request.description,
            request.body,
            request.tagList,
            authenticatedUser.userId
        )
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleWrapper)
    }

    override fun updateArticle(
        slug: String,
        request: UpdateArticleRequest,
        authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper {
        val article = updateArticleService.updateArticle(
            slug = Slug.from(slug),
            title = request.title,
            description = request.description,
            body = request.body,
        )
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleWrapper)
    }

    override fun deleteArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ) {
        deleteArticleService.deleteArticle(Slug.from(slug), authenticatedUser.userId)
    }

    override fun favoriteArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper {
        val article = favoriteArticleService.favoriteArticle(
            slug = Slug.from(slug),
            favoritingId = authenticatedUser.userId
        )
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleWrapper)
    }

    override fun unFavoriteArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ): ArticleWrapper {
        val article = favoriteArticleService.unfavoriteArticle(
            slug = Slug.from(slug),
            unavoritingId = authenticatedUser.userId
        )
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleWrapper)
    }

    override fun feedArticles(request: FeedArticleRequest, authenticatedUser: AuthenticatedUser): ArticlesWrapper {
        val articles = getArticleService.feedArticles(
            userId = authenticatedUser.userId,
            limit = request.limit,
            offset = request.offset
        )
        return articleResponseAssembler.assembleArticlesResponse(articles, authenticatedUser)
    }
}