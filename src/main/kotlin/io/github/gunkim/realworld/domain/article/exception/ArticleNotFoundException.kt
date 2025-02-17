package io.github.gunkim.realworld.domain.article.exception

import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.share.DomainException

class ArticleNotFoundException(
    override val message: String
): DomainException(message) {
    companion object {
        fun fromSlug(slug: Slug) = ArticleNotFoundException("Article(slug: $slug) not found")
    }
}