package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.ArticleDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.TagJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import io.github.gunkim.realworld.share.IntegrationTest
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.DisplayName
import java.time.Instant
import java.util.UUID
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

@Tags("Integration Test")
@DisplayName("Articles Controller - Integration Test")
class ArticlesControllerIntegrationTest(
    articleDao: ArticleDao,
) : IntegrationTest() {
    init {
        "GET /api/articles - Get all articles" {
            val (_, token) = createUser("gunkim.author@gmail.com", "gunkim", "password")
            val (author, _) = createUser("gunkim@gmail.com", "author gunkim", "password")
            val articles = listOf(
                ArticleJpaEntity(
                    uuid = UUID.randomUUID(),
                    slug = "article-1",
                    title = "Article 1",
                    description = "Description 1",
                    body = "Body 1",
                    tags = listOf(
                        TagJpaEntity(name = "tag1"),
                        TagJpaEntity(name = "tag2"),
                    ),
                    author = UserJpaEntity.from(author),
                    createdAt = Instant.now(),
                )
            )
            articleDao.saveAll(articles)

            mockMvc.get("/api/articles") {
                header(HttpHeaders.AUTHORIZATION, token)
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
                jsonPath("$.articles[0].slug") { value(articles[0].slug) }
                jsonPath("$.articles[0].createdAt") { exists() }
                jsonPath("$.articles[0].updatedAt") { exists() }
            }.andDo { print() }
        }
    }
}