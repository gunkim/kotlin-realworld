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
        criteriaBuilder.and(
            *mutableListOf<Predicate>().apply {
                where(root, criteriaBuilder) {
                    equalTagName(tag)
                    equalAuthorName(author)
                    equalFavoritedUsername(favoritedUsername)
                }
            }.toTypedArray()
        )
    }
}

private class PredicateContext(
    val root: Root<ArticleEntity>,
    val criteriaBuilder: CriteriaBuilder,
    val predicates: MutableList<Predicate>,
) {
    fun equalTagName(tag: String?) {
        if (tag == null) return
        val tagJoin = root.join<Any, Any>("tagEntities", JoinType.LEFT)
        val tagName = tagJoin.get<String>("tagEntity").get<String>("name")
        predicates.add(criteriaBuilder.equal(tagName, tag))
    }

    fun equalAuthorName(author: String?) {
        if (author == null) return
        predicates.add(
            criteriaBuilder.equal(root.get<Any>("authorEntity").get<String>("name"), author)
        )
    }

    fun equalFavoritedUsername(favoritedUsername: String?) {
        if (favoritedUsername == null) return
        val favoritedJoin = root.join<Any, Any>("favoriteEntities", JoinType.LEFT)
        val userJoin = favoritedJoin.join<Any, Any>("userEntity", JoinType.LEFT)
        val favoritedName = userJoin.get<String>("name")
        predicates.add(criteriaBuilder.equal(favoritedName, favoritedUsername))
    }
}

private inline fun MutableList<Predicate>.where(
    root: Root<ArticleEntity>,
    criteriaBuilder: CriteriaBuilder,
    block: PredicateContext.() -> Unit,
) {
    PredicateContext(root, criteriaBuilder, this).block()
}