package io.github.gunkim.realworld.domain.article.repository

import io.github.gunkim.realworld.domain.article.model.Article

interface ArticleRepository: ArticleReadRepository {
    fun save(article: Article): Article
    fun delete(article: Article)
}