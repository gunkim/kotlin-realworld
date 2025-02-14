package io.github.gunkim.realworld.domain.article

import java.util.UUID

interface ArticleReadRepository {
    fun find(
        tag: String?,
        author: String?,
        limit: Int,
        offset: Int,
    ): List<Article>

    fun getCountAllByArticleUuids(articleUuids: List<UUID>): List<ArticleCountProjection>

    fun getFavoritesArticles(userUuid: UUID): List<UUID>
}
