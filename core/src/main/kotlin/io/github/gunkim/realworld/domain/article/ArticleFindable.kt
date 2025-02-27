package io.github.gunkim.realworld.domain.article

import io.github.gunkim.realworld.domain.article.exception.ArticleNotFoundException
import io.github.gunkim.realworld.domain.article.model.Article
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.repository.ArticleRepository

interface ArticleFindable {
    val articleRepository: ArticleRepository

    fun findBySlug(slug: Slug): Article =
        articleRepository.findBySlug(slug)
            ?: throw ArticleNotFoundException.fromSlug(slug)
}