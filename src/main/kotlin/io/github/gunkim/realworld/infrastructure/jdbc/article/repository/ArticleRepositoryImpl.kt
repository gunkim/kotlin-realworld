package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.Article
import io.github.gunkim.realworld.domain.article.ArticleReadRepository
import io.github.gunkim.realworld.domain.article.ArticleRepository
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.ArticleDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.TagDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.TagJpaEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
class ArticleRepositoryImpl(
    @Qualifier("articleReadRepositoryImpl") private val articleReadRepository: ArticleReadRepository,
    private val articleDao: ArticleDao,
    private val tagDao: TagDao,
) : ArticleRepository, ArticleReadRepository by articleReadRepository {
    override fun save(article: Article): Article {
        val existingTags = tagDao.findByNameIn(article.tags.map { it.name })
        val existingTagNames = existingTags.map { it.name }.toSet()

        val newTags = article.tags.filterNot { existingTagNames.contains(it.name) }
            .map { TagJpaEntity.from(it.name) }

        val allTags = existingTags + newTags
        return articleDao.save(ArticleJpaEntity.from(article, allTags))
    }
}
