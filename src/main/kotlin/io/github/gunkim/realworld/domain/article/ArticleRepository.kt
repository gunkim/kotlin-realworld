package io.github.gunkim.realworld.domain.article

interface ArticleRepository: ArticleReadRepository {
    fun save(article: Article): Article
}