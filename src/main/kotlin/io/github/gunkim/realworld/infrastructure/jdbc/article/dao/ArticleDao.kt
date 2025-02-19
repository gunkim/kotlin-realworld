package io.github.gunkim.realworld.infrastructure.jdbc.article.dao

import io.github.gunkim.realworld.domain.article.model.ArticleCountProjection
import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleJpaEntity
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleDao : JpaRepository<ArticleJpaEntity, Long> {

    /**
     * TODO: Consider using a query builder for optional conditions.
     */
    @Query(
        """
        SELECT DISTINCT a 
        FROM article a 
        LEFT JOIN a.articleTagJpaEntities at
        INNER JOIN at.tag t
        WHERE (:tag IS NULL OR t.name = :tag)
          AND (:author IS NULL OR a.author.name = :author)
        """
    )
    fun findArticlesByTagAndAuthor(
        @Param("tag") tag: String?,
        @Param("author") author: String?,
        pageable: Pageable,
    ): List<ArticleJpaEntity>

    @Query(
        """
        SELECT CAST(a.uuid AS char(36)) as uuid, COUNT(*) as count
        FROM article_favorite af
             INNER JOIN
             article a ON af.article_id = a.article_id
        WHERE a.uuid IN :articleUuids 
        GROUP BY a.uuid
        """, nativeQuery = true
    )
    fun getCountAllByArticleUuids(@Param("articleUuids") articleUuids: List<UUID>): List<ArticleCountProjection>

    @Query(
        """
            SELECT a.uuid
            FROM article a
                 INNER JOIN
                 article_favorite af ON af.articleId = a.articleId
                 INNER JOIN
                 users u ON u.userId = af.userId
            WHERE u.uuid = :userUuid
        """
    )
    fun getFavoritesArticles(userUuid: UUID): List<UUID>

    fun findBySlugValue(slug: String): ArticleJpaEntity?

    @Query(
        """
        SELECT a
        FROM users u
             INNER JOIN user_follow uf ON uf.followerId = u.userId
             INNER JOIN article a ON a.author.userId = uf.followeeId
        WHERE u.uuid = :userId
        """
    )
    fun findFeedArticles(userId: UUID, pageable: Pageable): List<ArticleJpaEntity>
}