package io.github.gunkim.realworld.infrastructure.jdbc.article.dao

import io.github.gunkim.realworld.domain.article.model.ArticleCountProjection
import io.github.gunkim.realworld.domain.article.model.ArticleId
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleJpaEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ArticleDao : JpaRepository<ArticleJpaEntity, Int> {

    /**
     * TODO: Consider using a query builder for optional conditions.
     */
    @Query(
        """
        SELECT DISTINCT a 
        FROM article a 
              LEFT JOIN a.articleTagJpaEntities at
              LEFT JOIN article_favorite af ON af.articleDatabaseId = a.databaseId
              LEFT JOIN users u ON u.databaseId = af.userDatabaseId
             INNER JOIN at.tag t
        WHERE (:tag IS NULL OR t.name = :tag)
          AND (:author IS NULL OR a.author.name = :author)
          AND (:favoritedUsername IS NULL OR u.name = :favoritedUsername)
        """
    )
    fun find(
        tag: String?,
        author: String?,
        favoritedUsername: String?,
        pageable: Pageable,
    ): List<ArticleJpaEntity>

    @Query(
        """
        SELECT a.id as articleId, COUNT(*) as count
        FROM article_favorite af
             INNER JOIN
             article a ON af.articleDatabaseId = a.databaseId
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
                 article_favorite af ON af.articleDatabaseId = a.databaseId
                 INNER JOIN
                 users u ON u.databaseId = af.userDatabaseId
            WHERE u.id = :userId
        """
    )
    fun getFavoritesArticles(userId: UserId): List<ArticleId>

    fun findBySlug(slug: Slug): ArticleJpaEntity?

    @Query(
        """
        SELECT a
        FROM users u
             INNER JOIN user_follow uf ON uf.followerUserDatabaseId = u.databaseId
             INNER JOIN article a ON a.author.databaseId = uf.followeeUserDatabaseId
        WHERE u.id = :userId
        """
    )
    fun findFeedArticles(userId: UserId, pageable: Pageable): List<ArticleJpaEntity>
}