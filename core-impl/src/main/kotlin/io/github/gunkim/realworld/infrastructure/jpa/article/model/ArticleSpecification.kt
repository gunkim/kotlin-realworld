package io.github.gunkim.realworld.infrastructure.jpa.article.model

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

fun buildArticleSpecification(
    tag: String?,
    author: String?,
    favoritedUsername: String?,
): Specification<ArticleEntity> {
    return Specification { root, query, criteriaBuilder ->
        query.distinct(true)
        criteriaBuilder.and(*mutableListOf<Predicate>().apply {
            equalTagName(tag, root, criteriaBuilder)
            equalAuthorName(author, root, criteriaBuilder)
            equalFavoritedUsername(favoritedUsername, root, criteriaBuilder)
        }.toTypedArray())
    }
}

private fun MutableList<Predicate>.equalTagName(
    tag: String?,
    root: Root<ArticleEntity>,
    criteriaBuilder: CriteriaBuilder,
) {
    if (tag == null) return

    val tagJoin = root.join<Any, Any>("tagEntities", JoinType.LEFT)
    val tagName = tagJoin.get<String>("tagEntity").get<String>("name")
    this.add(criteriaBuilder.equal(tagName, tag))
}

private fun MutableList<Predicate>.equalAuthorName(
    author: String?,
    root: Root<ArticleEntity>,
    criteriaBuilder: CriteriaBuilder,
) {
    if (author == null) return

    this.add(criteriaBuilder.equal(root.get<Any>("authorEntity").get<String>("name"), author))
}

private fun MutableList<Predicate>.equalFavoritedUsername(
    favoritedUsername: String?,
    root: Root<ArticleEntity>,
    criteriaBuilder: CriteriaBuilder,
) {
    if (favoritedUsername == null) return

    val favoritedJoin = root.join<Any, Any>("favoriteEntities", JoinType.LEFT)
    val userJoin = favoritedJoin.join<Any, Any>("userEntity", JoinType.LEFT)

    val favoritedName = userJoin.get<String>("name")
    this.add(criteriaBuilder.equal(favoritedName, favoritedUsername))
}