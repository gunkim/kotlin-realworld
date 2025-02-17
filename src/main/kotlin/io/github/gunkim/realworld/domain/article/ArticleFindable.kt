package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.article.exception.ArticleNotFoundException

interface ArticleFindable {
    val articleRepository: ArticleRepository

    fun findBySlug(slug: Slug): Article =
        articleRepository.findBySlug(slug)
            ?: throw ArticleNotFoundException.fromSlug(slug)
}