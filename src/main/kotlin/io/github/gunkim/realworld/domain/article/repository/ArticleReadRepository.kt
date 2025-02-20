package io.github.gunkim.realworld.domain.article.repository

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.user.model.UserId

interface ArticleReadRepository {
    fun find(
        tag: String?,
        author: String?,
        limit: Int,
        offset: Int,
    ): List<Article>

    fun findFeedArticles(userId: UserId, limit: Int, offset: Int): List<Article>
    fun getCountAllByArticleUuids(articleIds: List<ArticleId>): List<ArticleCountProjection>
    fun getFavoritesArticles(userId: UserId): List<ArticleId>
    fun findBySlug(slug: Slug): Article?
}
