package io.github.gunkim.realworld.infrastructure.jpa.article.repository

import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.repository.ArticleReadRepository
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.infrastructure.jpa.article.dao.ArticleDao
import io.github.gunkim.realworld.infrastructure.jpa.article.dao.ArticleFavoriteDao
import io.github.gunkim.realworld.infrastructure.jpa.article.dao.TagDao
import io.github.gunkim.realworld.infrastructure.jpa.article.model.ArticleEntity
import io.github.gunkim.realworld.infrastructure.jpa.article.model.ArticleTagEntity
import io.github.gunkim.realworld.infrastructure.jpa.article.model.FavoriteEntity
import io.github.gunkim.realworld.infrastructure.jpa.article.model.TagEntity
import io.github.gunkim.realworld.infrastructure.jpa.user.model.UserEntity
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
        val articleEntity = convertArticleToEntity(article)
        return articleDao.save(articleEntity)
    }

    override fun delete(article: Article) {
        articleDao.delete(convertArticleToEntity(article))
    }

    override fun favorite(article: Article, user: User) {
        val (articleEntity, userEntity) = mapToEntities(article, user)
        articleFavoriteDao.save(
            FavoriteEntity.of(
                articleEntity = articleEntity,
                userEntity = userEntity
            )
        )
    }

    override fun unFavorite(article: Article, user: User) {
        val (articleEntity, userEntity) = mapToEntities(article, user)
        articleFavoriteDao.deleteByArticleEntityAndUserEntity(
            articleEntity = articleEntity,
            userEntity = userEntity
        )
    }

    private fun mapToEntities(article: Article, user: User) =
        convertArticleToEntity(article) to UserEntity.from(user)

    private fun convertArticleToEntity(article: Article) =
        ArticleEntity.from(article, convertTagsToEntities(article))

    /**
     * Converts the `tags` associated with the provided `article` into a list of `ArticleTagJpaEntity`.
     * If the `article` is already an instance of `ArticleJpaEntity`, the existing `articleTagJpaEntities` are returned.
     * Otherwise, it retrieves existing tag entities from the database, identifies any new tags that aren't already present,
     * creates new tag entities, and combines both existing and new tag entities to generate the result.
     *
     * @param article The article whose tags need to be converted to JPA entities.
     * @return A list of `ArticleTagJpaEntity` objects corresponding to the tags of the article.
     */
    private fun convertTagsToEntities(article: Article): List<ArticleTagEntity> {
        if (article is ArticleEntity) return article.tagEntities

        val tags = article.tags
        val existingTagEntities = tagDao.findByNameIn(tags.map { it.name })
        val existingTagNames = existingTagEntities.map { it.name }.toSet()
        val newTagEntities = tags.filterNot { it.name in existingTagNames }
            .map { TagEntity.from(it.name) }

        return (existingTagEntities + newTagEntities)
            .map(ArticleTagEntity.Companion::fromTagEntity)
    }
}