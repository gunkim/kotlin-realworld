package io.github.gunkim.realworld.testcase.integration

import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.TagDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.TagJpaEntity
import io.github.gunkim.realworld.share.IntegrationTest
import io.kotest.core.annotation.DisplayName
import io.kotest.core.test.TestCase
import org.springframework.test.web.servlet.get

@DisplayName("Tags Controller - Integration Test")
class TagsControllerTest(
    private val tagDao: TagDao,
) : IntegrationTest() {
    override suspend fun beforeEachTest(testCase: TestCase) {
        tagDao.saveAll(
            listOf(
                TagJpaEntity.from("tag1"),
                TagJpaEntity.from("tag2")
            )
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