package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.ArticleReadRepository
import io.github.gunkim.realworld.domain.article.Slug
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.ArticleDao
import java.util.UUID
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class ArticleReadRepositoryImpl(
    private val articleDao: ArticleDao,
) : ArticleReadRepository {
    override fun find(
        tag: String?,
        author: String?,
        limit: Int,
        offset: Int,
    ): List<Article> {
        val page = offset / limit
        val pageable = PageRequest.of(page, limit)

        return articleDao.findArticlesByTagAndAuthor(tag, author, pageable)
    }

    override fun getCountAllByArticleUuids(articleUuids: List<UUID>): List<ArticleCountProjection> {
        return articleDao.getCountAllByArticleUuids(articleUuids)
    }

    override fun getFavoritesArticles(userUuid: UUID): List<UUID> {
        return articleDao.getFavoritesArticles(userUuid)
    }

    override fun findBySlug(slug: Slug): Article? =
        articleDao.findBySlugValue(slug.value)
}