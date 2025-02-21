package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleReadRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.ArticleDao
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

    override fun findFeedArticles(userId: UserId, limit: Int, offset: Int): List<Article> {
        val page = offset / limit
        val pageable = PageRequest.of(page, limit)

        return articleDao.findFeedArticles(userId, pageable)
    }

    override fun getCountAllByArticleIds(articleIds: List<ArticleId>): List<ArticleCountProjection> {
        return articleDao.getCountAllByArticleIds(articleIds)
    }

    override fun getFavoritesArticleIds(userId: UserId): List<ArticleId> {
        return articleDao.getFavoritesArticles(userId)
    }

    override fun findBySlug(slug: Slug): Article? =
        articleDao.findBySlug(slug)
}