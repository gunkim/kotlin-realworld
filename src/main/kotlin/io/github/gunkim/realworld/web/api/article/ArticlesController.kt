package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.application.DeleteArticleUseCase
import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.service.CreateArticleService
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.article.service.GetArticleService
import io.github.gunkim.realworld.domain.article.service.UpdateArticleService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.share.PagingRequest
import io.github.gunkim.realworld.web.api.article.model.request.CreateArticleRequest
import io.github.gunkim.realworld.web.api.article.model.request.GetArticlesRequest
import io.github.gunkim.realworld.web.api.article.model.request.UpdateArticleRequest
import io.github.gunkim.realworld.web.api.article.model.response.ArticleResponseWrapper
import io.github.gunkim.realworld.web.api.article.model.response.ArticlesResponse
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
    ): ArticlesResponse

    @GetMapping("/feed")
    fun feedArticles(
        @ModelAttribute request: PagingRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
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
    fun updateArticle(
        @PathVariable slug: String,
        @JsonRequest("article") request: UpdateArticleRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper

    @DeleteMapping("/{slug}")
    fun deleteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    )

    @PostMapping("/{slug}/favorite")
    fun favoriteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper

    @DeleteMapping("/{slug}/favorite")
    fun unFavoriteArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper
}

@RestController
class ArticlesController(
    private val getArticleService: GetArticleService,
    private val createArticleService: CreateArticleService,
    private val updateArticleService: UpdateArticleService,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val articleResponseAssembler: ArticleResponseAssembler,
    private val favoriteArticleService: FavoriteArticleService,
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
        return articleResponseAssembler.assembleArticlesResponse(articles, authenticatedUser)
    }

    override fun getArticle(slug: String): ArticleResponseWrapper {
        val article = getArticleService.getArticle(Slug(slug))
        return articleResponseAssembler.assembleArticleResponse(article, null)
            .let(::ArticleResponseWrapper)
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
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleResponseWrapper)
    }

    override fun updateArticle(
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
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleResponseWrapper)
    }

    override fun deleteArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ) {
        deleteArticleUseCase.deleteArticle(slug, authenticatedUser.uuid)
    }

    override fun favoriteArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper {
        val article = favoriteArticleService.favoriteArticle(
            slug = Slug(slug),
            userUuid = authenticatedUser.uuid
        )
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleResponseWrapper)
    }

    override fun unFavoriteArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ): ArticleResponseWrapper {
        val article = favoriteArticleService.unfavoriteArticle(
            slug = Slug(slug),
            userUuid = authenticatedUser.uuid
        )
        return articleResponseAssembler.assembleArticleResponse(article, authenticatedUser)
            .let(::ArticleResponseWrapper)
    }

    override fun feedArticles(request: PagingRequest, authenticatedUser: AuthenticatedUser): ArticlesResponse {
        val articles = getArticleService.feedArticles(
            authUuid = authenticatedUser.uuid,
            limit = request.limit,
            offset = request.offset
        )
        return articleResponseAssembler.assembleArticlesResponse(articles, authenticatedUser)
    }
}