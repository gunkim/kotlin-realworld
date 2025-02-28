package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.repository.ArticleReadRepository
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.ArticleDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.buildArticleSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ArticleReadRepositoryImpl(
    private val articleDao: ArticleDao,
) : ArticleReadRepository {
    override fun find(
        tag: String?,
        author: String?,
        favoritedUsername: String?,
        limit: Int,
        offset: Int,
    ): List<Article> {
        val page = calculatePage(offset, limit)
        val pageable = PageRequest.of(page, limit)

        val spec = buildArticleSpecification(tag, author, favoritedUsername)
        return articleDao.findAll(spec, pageable).content
    }

    override fun findFeedArticles(userId: UserId, limit: Int, offset: Int): List<Article> {
        val page = calculatePage(offset, limit)
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

    private fun calculatePage(offset: Int, limit: Int) = offset / limit
}
