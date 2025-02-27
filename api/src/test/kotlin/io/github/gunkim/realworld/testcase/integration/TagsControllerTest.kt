package io.github.gunkim.realworld.testcase.integration

import io.github.gunkim.realworld.domain.article.service.CreateArticleService
import io.github.gunkim.realworld.share.IntegrationTest
import io.kotest.core.annotation.DisplayName
import io.kotest.core.test.TestCase
import org.springframework.test.web.servlet.get

@DisplayName("Tags Controller - Integration Test")
class TagsControllerTest(
    private val createArticleService: CreateArticleService,
) : IntegrationTest() {
    override suspend fun beforeEachTest(testCase: TestCase) {
        val (user, _) = createUser("test@gmail.com", "test", "test@test")
        createArticleService.createArticle(
            title = "title",
            description = "description",
            body = "body",
            tagList = listOf("tag1", "tag2"),
            authorId = user.id
        )
    }

    init {
        "GET /api/tags - Return all tags" {
            mockMvc.get("/api/tags")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.tags") { isArray() }
                    jsonPath("$.tags[0]") { value("tag1") }
                    jsonPath("$.tags[1]") { value("tag2") }
                }
                .andDo { print() }
        }
    }
}