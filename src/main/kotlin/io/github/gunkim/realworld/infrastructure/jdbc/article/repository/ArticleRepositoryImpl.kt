package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.repository.ArticleReadRepository
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.ArticleDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.ArticleFavoriteDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.TagDao
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleFavoriteJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleTagJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.TagJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
class ArticleRepositoryImpl(
    @Qualifier("articleReadRepositoryImpl") private val articleReadRepository: ArticleReadRepository,
    private val articleDao: ArticleDao,
    private val tagDao: TagDao,
    private val articleFavoriteDao: ArticleFavoriteDao,
) : ArticleRepository, ArticleReadRepository by articleReadRepository {
    override fun save(article: Article): Article {
        val allTags = articleTagJpaEntities(article)
        return articleDao.save(ArticleJpaEntity.from(article, allTags))
    }

    override fun delete(article: Article) {
        articleDao.delete(ArticleJpaEntity.from(article, articleTagJpaEntities(article)))
    }

    override fun favorite(article: Article, user: User) {
        val articleEntity = ArticleJpaEntity.from(article, articleTagJpaEntities(article))
        val userEntity = UserJpaEntity.from(user)

        articleFavoriteDao.save(
            ArticleFavoriteJpaEntity.of(
                articleEntity.databaseId!!,
                userEntity.databaseId!!
            )
        )
    }

    override fun unFavorite(article: Article, user: User) {
        val articleEntity = ArticleJpaEntity.from(article, articleTagJpaEntities(article))
        val userEntity = UserJpaEntity.from(user)

        articleFavoriteDao.deleteByArticleDatabaseIdAndUserDatabaseId(
            articleEntity.databaseId!!,
            userEntity.databaseId!!
        )
    }

    private fun articleTagJpaEntities(article: Article): List<ArticleTagJpaEntity> {
        if (article is ArticleJpaEntity) {
            return article.articleTagJpaEntities
        }
        val existingTags = tagDao.findByNameIn(article.tags.map { it.name })
        val existingTagNames = existingTags.map { it.name }.toSet()

        val newTags = article.tags.filterNot { existingTagNames.contains(it.name) }
            .map { TagJpaEntity.from(it.name) }

        val allTags = (existingTags + newTags).map(ArticleTagJpaEntity.Companion::fromTagEntity)
        return allTags
    }
}
