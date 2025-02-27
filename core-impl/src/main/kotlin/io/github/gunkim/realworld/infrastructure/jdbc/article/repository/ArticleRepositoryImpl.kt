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
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ArticleRepositoryImpl(
    @Qualifier("articleReadRepositoryImpl") private val articleReadRepository: ArticleReadRepository,
    private val articleDao: ArticleDao,
    private val tagDao: TagDao,
    private val articleFavoriteDao: ArticleFavoriteDao,
) : ArticleRepository, ArticleReadRepository by articleReadRepository {

    override fun save(article: Article): Article {
        val articleJpaEntity = convertArticleToJpaEntity(article)
        return articleDao.save(articleJpaEntity)
    }

    override fun delete(article: Article) {
        articleDao.delete(convertArticleToJpaEntity(article))
    }

    override fun favorite(article: Article, user: User) {
        val (articleJpaEntity, userJpaEntity) = mapToJpaEntities(article, user)
        articleFavoriteDao.save(
            ArticleFavoriteJpaEntity.of(
                articleJpaEntity.databaseId!!,
                userJpaEntity.databaseId!!
            )
        )
    }

    override fun unFavorite(article: Article, user: User) {
        val (articleJpaEntity, userJpaEntity) = mapToJpaEntities(article, user)
        articleFavoriteDao.deleteByArticleDatabaseIdAndUserDatabaseId(
            articleJpaEntity.databaseId!!,
            userJpaEntity.databaseId!!
        )
    }

    private fun mapToJpaEntities(article: Article, user: User) =
        convertArticleToJpaEntity(article) to io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity.from(user)

    private fun convertArticleToJpaEntity(article: Article) =
        ArticleJpaEntity.from(article, convertTagsToJpaEntities(article))

    /**
     * Converts the `tags` associated with the provided `article` into a list of `ArticleTagJpaEntity`.
     * If the `article` is already an instance of `ArticleJpaEntity`, the existing `articleTagJpaEntities` are returned.
     * Otherwise, it retrieves existing tag entities from the database, identifies any new tags that aren't already present,
     * creates new tag entities, and combines both existing and new tag entities to generate the result.
     *
     * @param article The article whose tags need to be converted to JPA entities.
     * @return A list of `ArticleTagJpaEntity` objects corresponding to the tags of the article.
     */
    private fun convertTagsToJpaEntities(article: Article): List<ArticleTagJpaEntity> {
        if (article is ArticleJpaEntity) return article.articleTagJpaEntities

        val tags = article.tags
        val existingTagEntities = tagDao.findByNameIn(tags.map { it.name })
        val existingTagNames = existingTagEntities.map { it.name }.toSet()
        val newTagEntities = tags.filterNot { it.name in existingTagNames }
            .map { TagJpaEntity.from(it.name) }

        return (existingTagEntities + newTagEntities)
            .map(ArticleTagJpaEntity.Companion::fromTagEntity)
    }
}