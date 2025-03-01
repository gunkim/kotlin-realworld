package io.github.gunkim.realworld.infrastructure.jpa.article.dao

import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleCountProjection
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jpa.article.model.ArticleEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface ArticleDao : JpaRepository<ArticleEntity, Int>, JpaSpecificationExecutor<ArticleEntity> {
    @Query(
        """
        SELECT a.id as articleId, COUNT(*) as count
        FROM article a
             INNER JOIN
             a.favoriteEntities af
        WHERE a.id IN :articleIds 
        GROUP BY a.id
        """
    )
    fun getCountAllByArticleIds(articleIds: List<ArticleId>): List<ArticleCountProjection>

    @Query(
        """
        SELECT a.id
        FROM article a
             INNER JOIN
             a.favoriteEntities af
             INNER JOIN
             af.userEntity u
        WHERE u.id = :userId
        """
    )
    fun getFavoritesArticles(userId: UserId): List<ArticleId>

    fun findBySlug(slug: Slug): ArticleEntity?

    @Query(
        """
        SELECT a
        FROM users u
             INNER JOIN user_follow uf ON uf.followerUserDatabaseId = u.databaseId
             INNER JOIN article a ON a.authorEntity.databaseId = uf.followeeUserDatabaseId
        WHERE u.id = :userId
        """
    )
    fun findFeedArticles(userId: UserId, pageable: Pageable): List<ArticleEntity>
}