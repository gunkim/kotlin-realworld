package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import io.github.gunkim.realworld.infrastructure.jdbc.article.model.ArticleFavoriteJpaEntity
import io.github.gunkim.realworld.infrastructure.jdbc.user.model.UserJpaEntity
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

fun buildArticleSpecification(
    tag: String?,
    author: String?,
    favoritedUsername: String?,
): Specification<ArticleJpaEntity> {
    return Specification { root, query, criteriaBuilder ->
        query.distinct(true)
        criteriaBuilder.and(*mutableListOf<Predicate>().apply {
            equalTagName(tag, root, criteriaBuilder)
            equalAuthorName(author, root, criteriaBuilder)
            equalFavoritedUsername(favoritedUsername, root, query, criteriaBuilder)
        }.toTypedArray())
    }
}

private fun MutableList<Predicate>.equalTagName(
    tag: String?,
    root: Root<ArticleJpaEntity>,
    criteriaBuilder: CriteriaBuilder,
) {
    if (tag == null) return

    val tagJoin = root.join<Any, Any>("articleTagJpaEntities", JoinType.LEFT)
    val tagName = tagJoin.get<String>("tag").get<String>("name")
    this.add(criteriaBuilder.equal(tagName, tag))
}

private fun MutableList<Predicate>.equalAuthorName(
    author: String?,
    root: Root<ArticleJpaEntity>,
    criteriaBuilder: CriteriaBuilder,
) {
    if (author == null) return

    this.add(criteriaBuilder.equal(root.get<Any>("author").get<String>("name"), author))
}

private fun MutableList<Predicate>.equalFavoritedUsername(
    favoritedUsername: String?,
    root: Root<ArticleJpaEntity>,
    query: CriteriaQuery<*>,
    criteriaBuilder: CriteriaBuilder,
) {
    if (favoritedUsername == null) return

    val favoriteRoot = query.from(ArticleFavoriteJpaEntity::class.java)
    val userRoot = query.from(UserJpaEntity::class.java)

    // Since there is no direct relationship between the entities, an implicit join is performed via the WHERE clause.
    val articleMatch = criteriaBuilder.equal(
        favoriteRoot.get<Int>("articleDatabaseId"),
        root.get<Int>("databaseId")
    )
    val userMatch = criteriaBuilder.equal(
        favoriteRoot.get<Int>("userDatabaseId"),
        userRoot.get<Int>("databaseId")
    )

    val usernameMatch = criteriaBuilder.equal(
        userRoot.get<String>("name"),
        favoritedUsername
    )

    this.add(criteriaBuilder.and(articleMatch, userMatch, usernameMatch))
}