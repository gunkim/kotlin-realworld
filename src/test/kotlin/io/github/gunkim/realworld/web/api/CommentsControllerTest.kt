package io.github.gunkim.realworld.web.api

import io.github.gunkim.realworld.domain.comment.service.AddCommentService
import io.github.gunkim.realworld.domain.article.service.CreateArticleService
import io.github.gunkim.realworld.share.IntegrationTest
import io.github.gunkim.realworld.web.api.article.model.request.AddCommentRequest
import io.kotest.core.annotation.DisplayName
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
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
                header("Authorization", authToken)
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
                header("Authorization", authToken)
            }.andExpect {
                status { isOk() }
            }.andDo { print()}
        }
    }
}