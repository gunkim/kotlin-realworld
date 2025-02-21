package io.github.gunkim.realworld.test.case.integration.api

import io.github.gunkim.realworld.domain.article.exception.ArticleNotFoundException
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.service.CreateArticleService
import io.github.gunkim.realworld.domain.article.service.FavoriteArticleService
import io.github.gunkim.realworld.domain.article.service.GetArticleService
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.test.util.IntegrationTest
import io.github.gunkim.realworld.web.api.article.model.request.CreateArticleRequest
import io.github.gunkim.realworld.web.api.article.model.request.UpdateArticleRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.DisplayName
import io.kotest.core.test.TestCase
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@DisplayName("Articles Controller - Integration Test")
class ArticlesControllerIntegrationTest(
    private val createArticleService: CreateArticleService,
    private val getArticleService: GetArticleService,
    private val favoriteArticleService: FavoriteArticleService,
    private val followUserService: FollowUserService,
) : IntegrationTest() {
    lateinit var authUser: User
    lateinit var authToken: String

    lateinit var author: User
    lateinit var authorToken: String

    lateinit var articles: List<Article>

    override suspend fun beforeEachTest(testCase: TestCase) {
        val (authUser, token) = createUser("gunkim.author@gmail.com", "gunkim", "password")
        val (author, authorToken) = createUser("gunkim@gmail.com", "author gunkim", "password")
        val articles = listOf(
            createArticleService.createArticle(
                title = "Article Title",
                description = "Article Description",
                body = "Article Body",
                tagList = listOf("tag1", "tag2"),
                authorId = author.id
            )
        )
        this.authToken = token
        this.authUser = authUser
        this.articles = articles
        this.author = author
        this.authorToken = authorToken
    }

    init {
        "GET /api/articles - Get all articles" {
            mockMvc.get("/api/articles") {
                header(HttpHeaders.AUTHORIZATION, authToken)
            }.andExpect {
                status { isOk() }
                jsonPath("$.articles.length()") { value(articles.size) }
                jsonPath("$.articles[0].author.username") { value(author.name) }
                jsonPath("$.articles[0].favoritesCount") { value(0) }
                jsonPath("$.articles[0].body") { value(articles[0].body) }
                jsonPath("$.articles[0].tagList[0]") { value(articles[0].tags[0].name) }
                jsonPath("$.articles[0].tagList[1]") { value(articles[0].tags[1].name) }
                jsonPath("$.articles[0].title") { value(articles[0].title) }
                jsonPath("$.articles[0].description") { value(articles[0].description) }
                jsonPath("$.articles[0].slug") { value(articles[0].slug.value) }
                jsonPath("$.articlesCount") { value(articles.size) }
                jsonPath("$.articles[0].createdAt") { exists() }
                jsonPath("$.articles[0].updatedAt") { exists() }
            }.andDo { print() }
        }

        "GET /api/articles/:slug - Get a single article" {
            mockMvc.get("/api/articles/${articles[0].slug}")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.article.author.username") { value(author.name) }
                    jsonPath("$.article.favoritesCount") { value(0) }
                    jsonPath("$.article.body") { value(articles[0].body) }
                    jsonPath("$.article.tagList[0]") { value(articles[0].tags[0].name) }
                    jsonPath("$.article.tagList[1]") { value(articles[0].tags[1].name) }
                    jsonPath("$.article.title") { value(articles[0].title) }
                    jsonPath("$.article.description") { value(articles[0].description) }
                    jsonPath("$.article.slug") { value(articles[0].slug.value) }
                }.andDo { print() }
        }

        "GET /api/articles/feed - Get feed articles" {
            followUserService.followUser(
                authUser.id,
                author.name
            )

            mockMvc.get("/api/articles/feed") {
                header(HttpHeaders.AUTHORIZATION, authToken)
            }.andExpect {
                status { isOk() }
                jsonPath("$.articles.length()") { value(articles.size) }
                jsonPath("$.articles[0].author.username") { value(author.name) }
                jsonPath("$.articles[0].favoritesCount") { value(0) }
                jsonPath("$.articles[0].body") { value(articles[0].body) }
                jsonPath("$.articles[0].tagList[0]") { value(articles[0].tags[0].name) }
                jsonPath("$.articles[0].tagList[1]") { value(articles[0].tags[1].name) }
                jsonPath("$.articles[0].title") { value(articles[0].title) }
                jsonPath("$.articles[0].description") { value(articles[0].description) }
                jsonPath("$.articles[0].slug") { value(articles[0].slug.value) }
                jsonPath("$.articlesCount") { value(articles.size) }
                jsonPath("$.articles[0].createdAt") { exists() }
                jsonPath("$.articles[0].updatedAt") { exists() }
            }.andDo { print() }
        }

        "POST /api/articles - Create an article" {
            val request = CreateArticleRequest(
                title = "New Article",
                description = "New Article Description",
                body = "This is the body of the new article.",
                tagList = listOf("tag1", "tag3")
            )

            val requestBody = mapOf("article" to request)
            val requestJson = toJsonString(requestBody)

            mockMvc.post("/api/articles") {
                contentType = org.springframework.http.MediaType.APPLICATION_JSON
                header(HttpHeaders.AUTHORIZATION, authToken)
                content = requestJson
            }.andExpect {
                status { isCreated() }
                jsonPath("$.article.slug") { exists() }
                jsonPath("$.article.title") { value(request.title) }
                jsonPath("$.article.description") { value(request.description) }
                jsonPath("$.article.body") { value(request.body) }
                jsonPath("$.article.tagList[0]") { value(request.tagList[0]) }
                jsonPath("$.article.tagList[1]") { value(request.tagList[1]) }
                jsonPath("$.article.author.username") { value(authUser.name) }
                jsonPath("$.article.createdAt") { exists() }
                jsonPath("$.article.updatedAt") { exists() }
            }.andDo { print() }
        }

        "PUT /api/articles/:slug - Update an article" {
            val request = UpdateArticleRequest(
                title = "Updated Title",
                description = "Updated Description",
                body = "Updated Body"
            )

            val requestBody = mapOf("article" to request)
            val requestJson = toJsonString(requestBody)

            mockMvc.put("/api/articles/${articles[0].slug}") {
                contentType = org.springframework.http.MediaType.APPLICATION_JSON
                header(HttpHeaders.AUTHORIZATION, authToken)
                content = requestJson
            }.andExpect {
                status { isOk() }
                jsonPath("$.article.title") { value(request.title) }
                jsonPath("$.article.description") { value(request.description) }
                jsonPath("$.article.body") { value(request.body) }
                jsonPath("$.article.tagList[0]") { value(articles[0].tags[0].name) }
                jsonPath("$.article.tagList[1]") { value(articles[0].tags[1].name) }
            }.andDo { print() }
        }

        "DELETE /api/articles/:slug - Delete an article" {
            mockMvc.delete("/api/articles/${articles[0].slug}") {
                header(HttpHeaders.AUTHORIZATION, authorToken)
            }.andExpect {
                status { isOk() }
                assertArticleDeletionThrowsException()
            }.andDo { print() }
        }

        "POST /api/articles/:slug/favorite" {
            mockMvc.post("/api/articles/${articles[0].slug}/favorite") {
                header(HttpHeaders.AUTHORIZATION, authToken)
            }.andExpect {
                status { isOk() }
                jsonPath("$.article.favoritesCount") { value(1) }
                jsonPath("$.article.favorited") { value(true) }
                jsonPath("$.article.slug") { exists() }
                jsonPath("$.article.title") { value(articles[0].title) }
                jsonPath("$.article.description") { value(articles[0].description) }
                jsonPath("$.article.body") { value(articles[0].body) }
                jsonPath("$.article.tagList[0]") { value(articles[0].tags[0].name) }
                jsonPath("$.article.tagList[1]") { value(articles[0].tags[1].name) }
                jsonPath("$.article.author.username") { value(author.name) }
                jsonPath("$.article.createdAt") { exists() }
                jsonPath("$.article.updatedAt") { exists() }
            }.andDo { print() }
        }

        "DELETE /api/articles/:slug/favorite" {
            favoriteArticleService.favoriteArticle(
                articles[0].slug,
                authUser.id
            )
            mockMvc.delete("/api/articles/${articles[0].slug}/favorite") {
                header(HttpHeaders.AUTHORIZATION, authToken)
            }.andExpect {
                status { isOk() }
                jsonPath("$.article.favoritesCount") { value(0) }
                jsonPath("$.article.favorited") { value(false) }
                jsonPath("$.article.slug") { exists() }
                jsonPath("$.article.title") { value(articles[0].title) }
                jsonPath("$.article.description") { value(articles[0].description) }
                jsonPath("$.article.body") { value(articles[0].body) }
                jsonPath("$.article.tagList[0]") { value(articles[0].tags[0].name) }
                jsonPath("$.article.tagList[1]") { value(articles[0].tags[1].name) }
                jsonPath("$.article.author.username") { value(author.name) }
                jsonPath("$.article.createdAt") { exists() }
                jsonPath("$.article.updatedAt") { exists() }
            }
        }
    }

    private fun assertArticleDeletionThrowsException() {
        shouldThrow<ArticleNotFoundException> {
            getArticleService.findBySlug(articles[0].slug)
        }
    }
}