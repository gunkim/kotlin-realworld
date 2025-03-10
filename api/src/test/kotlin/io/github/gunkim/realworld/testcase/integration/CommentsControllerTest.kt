package io.github.gunkim.realworld.testcase.integration

import io.github.gunkim.realworld.domain.article.service.CreateArticleService
import io.github.gunkim.realworld.domain.comment.service.AddCommentService
import io.github.gunkim.realworld.share.IntegrationTest
import io.github.gunkim.realworld.api.comment.model.request.AddCommentRequest
import io.kotest.core.annotation.DisplayName
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@DisplayName("Comments Controller - Integration Test")
class CommentsControllerTest(
    private val createArticleService: CreateArticleService,
    private val createCommentService: AddCommentService,
) : IntegrationTest() {
    init {
        "POST /api/articles/:slug/comments - Add Comment" {
            val (authUser, authToken) = createUser(
                email = "gunkim.dev@gmail.com",
                password = "<PASSWORD>",
                username = "test"
            )
            val article = createArticleService.createArticle(
                "test-title",
                "test-description",
                "test-body",
                listOf(),
                authUser.id
            )

            val request = AddCommentRequest(
                body = "test comment"
            )

            val requestJson = toJsonString(mapOf("comment" to request))
            mockMvc.post("/api/articles/${article.slug}/comments") {
                header(authHeaderName, authToken)
                content = requestJson
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isCreated() }
                jsonPath("$.comment.id") { exists() }
                jsonPath("$.comment.createdAt") { exists() }
                jsonPath("$.comment.updatedAt") { exists() }
                jsonPath("$.comment.body") { value(request.body) }
                jsonPath("$.comment.author.username") { value(authUser.name) }
                jsonPath("$.comment.author.bio") { value(authUser.bio) }
                jsonPath("$.comment.author.image") { value(authUser.image) }
                jsonPath("$.comment.author.following") { value(false) }
            }.andDo { print() }
        }

        "DELETE /api/articles/:slug/comments/:commentId - Delete Comment" {
            val (authUser, authToken) = createUser(
                email = "gunkim.dev@gmail.com",
                password = "<PASSWORD>",
                username = "test"
            )
            val article = createArticleService.createArticle(
                "test-title",
                "test-description",
                "test-body",
                listOf(),
                authUser.id
            )
            val comment = createCommentService.addComment(
                article.slug,
                "Hello World!",
                authUser.id
            )

            mockMvc.delete("/api/articles/${article.slug}/comments/${comment.id}") {
                header(authHeaderName, authToken)
            }.andExpect {
                status { isOk() }
            }.andDo { print() }
        }

        "GET /api/articles/:slug/comments - Get Comments" {
            val (authUser, authToken) = createUser(
                email = "gunkim.dev@gmail.com",
                password = "<PASSWORD>",
                username = "test"
            )
            val article = createArticleService.createArticle(
                "test-title",
                "test-description",
                "test-body",
                listOf(),
                authUser.id
            )
            val comment = createCommentService.addComment(
                article.slug,
                "Hello World!",
                authUser.id
            )

            mockMvc.get("/api/articles/${article.slug}/comments") {
                header(authHeaderName, authToken)
            }.andExpect {
                status { isOk() }
                jsonPath("$.comments") { isArray() }
                jsonPath("$.comments.length()") { value(1) }
                jsonPath("$.comments[0].id") { value(comment.id.toString()) }
                jsonPath("$.comments[0].body") { value(comment.body) }
                jsonPath("$.comments[0].createdAt") { exists() }
                jsonPath("$.comments[0].updatedAt") { exists() }
                jsonPath("$.comments[0].author.username") { value(authUser.name) }
                jsonPath("$.comments[0].author.bio") { value(authUser.bio) }
                jsonPath("$.comments[0].author.image") { value(authUser.image) }
                jsonPath("$.comments[0].author.following") { value(false) }
            }.andDo { print() }
        }
    }
}